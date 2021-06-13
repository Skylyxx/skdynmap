package fr.skylyxx.skdynmap;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.skylyxx.skdynmap.commands.CmdSkDynmap;
import fr.skylyxx.skdynmap.skript.events.EventRender;
import fr.skylyxx.skdynmap.utils.Metrics;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class SkDynmap extends JavaPlugin {

    private static SkDynmap INSTANCE;
    public SkriptAddon addon;

    public File storageFile;
    public CustomYamlConfig storageYaml;

    public Plugin dynmap;
    public MarkerAPI markerAPI;
    public MarkerSet markerSet;

    public HashMap<String, DynmapArea> dynmapAreas = new HashMap<String, DynmapArea>();
    public HashMap<String, DynmapMarker> dynmapMarkers = new HashMap<String, DynmapMarker>();

    public static SkDynmap getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 9273);
        INSTANCE = this;
        final PluginManager pm = Bukkit.getPluginManager();
        final Plugin skript = pm.getPlugin("Skript");
        dynmap = pm.getPlugin("dynmap");
        if (skript == null || !skript.isEnabled()) {
            Logger.severe("Skript dependency was not found ! Disabling...");
            pm.disablePlugin(this);
            return;
        }
        if (dynmap == null || !dynmap.isEnabled()) {
            Logger.severe("Dynmap dependency was not found ! Disabling...");
            pm.disablePlugin(this);
            return;
        }
        try {
            initConfig();
        } catch (IOException | InvalidConfigurationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (!(loadSkript() && loadDynmap())) {
            pm.disablePlugin(this);
        }
        try {
            reloadStorageConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        PluginCommand skDynmapCommand = getCommand("skdynmap");
        skDynmapCommand.setExecutor(new CmdSkDynmap());
        skDynmapCommand.setPermission("skdynmap.use");
        skDynmapCommand.setPermissionMessage("&cSorry but you don't have the required permission !");

        try {
            checkForUpdates();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        saveStorageYaml();
    }

    public boolean checkForUpdates() throws IOException {
        URL url = new URL("https://api.github.com/repos/Skylyxx/skdynmap/releases/latest");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(bufferedReader, JsonObject.class);
        Version reference = new Version(jsonObject.get("tag_name").getAsString());
        Version current = new Version(getDescription().getVersion());
        if (current.compareTo(reference) < 0)
            getLogger().warning("New version is available (" + jsonObject.get("tag_name") + ")! Download it at https://github.com/Skylyxx/skdynmap/releases/latest !");
        else
            getLogger().info("You are running the latest version of SkDynmap.");
        return current.compareTo(reference) < 0;
    }

    private void initConfig() throws IOException, InvalidConfigurationException, IllegalAccessException {
        Config.load();
        storageFile = new File(getDataFolder() + File.separator + "storage.yml");
        if (!storageFile.exists()) {
            storageFile.getParentFile().mkdirs();
            storageFile.createNewFile();
        }
    }


    public void reloadStorageConfig() throws IOException, InvalidConfigurationException {
        storageYaml = new CustomYamlConfig();
        storageYaml.load(storageFile);
        dynmapAreas.clear();
        if (storageYaml.isSet("areas.")) {
            storageYaml.getConfigurationSection("areas").getKeys(false).forEach(id ->
                    dynmapAreas.put(id, getStorageYaml().getArea("areas." + id))
            );
        }
        dynmapMarkers.clear();
        if (storageYaml.isSet("markers.")) {
            storageYaml.getConfigurationSection("markers").getKeys(false).forEach(id ->
                    dynmapMarkers.put(id, new DynmapMarker(id))
            );
        }
    }

    public CustomYamlConfig getStorageYaml() {
        return storageYaml;
    }

    public void saveStorageYaml() {
        if(storageFile == null)
            return;
        storageYaml = new CustomYamlConfig();
        dynmapAreas.forEach((id, dynmapArea) ->
                storageYaml.setArea("areas." + id, dynmapArea)
        );
        dynmapMarkers.forEach((id, dynmapMarker) ->
                storageYaml.setMarker("markers." + id, dynmapMarker)
        );
        try {
            if(!storageFile.exists())
                if(storageFile.getParentFile().mkdirs())
                    storageFile.createNewFile();
            storageYaml.save(storageFile);
            Logger.info("File storage.yml was saved successfully", true);
        } catch (IOException e) {
            Logger.severe("There was an error while saving storage.yml !");
            e.printStackTrace();
        }

    }

    public void reloadSkDynmapConfig() throws IOException, InvalidConfigurationException, IllegalAccessException {
        reloadConfig();
        Config.load();
        reloadStorageConfig();
        Logger.info("Config has been reloaded !", true);
    }

    private boolean loadSkript() {
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.skylyxx.skdynmap.skript");
        } catch (IOException e) {
            Logger.severe("Unable to load SkDynmap's syntaxes ! Disabling...");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadDynmap() {
        dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        markerAPI = ((DynmapCommonAPI) dynmap).getMarkerAPI();
        if (markerAPI == null) {
            Logger.severe("There was an error while loading MarkerAPI ! Disabling...");
            return false;
        }
        markerSet = markerAPI.getMarkerSet("skdynmap.markerset");
        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet("skdynmap.markerset", "SkDynmap", null, false);
        } else {
            markerSet.setMarkerSetLabel("SkDynmap");
        }
        markerSet.setMinZoom(0);
        markerSet.setLayerPriority(10);
        markerSet.setHideByDefault(false);
        int taskInterval = Config.UPDATE_INTERVAL.<Integer>get();
        if (taskInterval > 0) {
            int renderTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                EventRender event = new EventRender();
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    Util.renderAllAreas();
                    Util.renderAllMarkers();
                }
            }, 100, taskInterval * 20L);
        }
        return true;
    }

    public boolean isDebugMode() {
        return Config.isLoaded() && Config.DEBUG_MODE.<Boolean>get();
    }

    public MarkerSet getMarkerSet() {
        return markerSet;
    }

    public MarkerAPI getMarkerAPI() {
        return markerAPI;
    }
}
