package fr.skylyxx.skdynmap;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.skylyxx.skdynmap.commands.CmdSkDynmap;
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

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class SkDynmap extends JavaPlugin {

    private static SkDynmap INSTANCE;
    public SkriptAddon addon;

    public File storageFile;
    public CustomYamlConfig storageYaml;

    public DynmapCommonAPI dynmapCommonAPI;
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
        final Plugin SKRIPT = pm.getPlugin("Skript");
        final Plugin DYNMAP = pm.getPlugin("dynmap");
        if (SKRIPT == null || !SKRIPT.isEnabled()) {
            Logger.severe("Skript dependency was not found ! Disabling...");
            pm.disablePlugin(this);
            return;
        }
        if (DYNMAP == null || !DYNMAP.isEnabled()) {
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

    }

    @Override
    public void onDisable() {
        saveStorageYaml();
    }

    @Nullable
    public String checkForUpdates() throws IOException {
        URL url = new URL("https://pastebin.com/raw/ZECi01d9");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        bufferedReader.close();
        String result = stringBuilder.toString();
        if (!result.equalsIgnoreCase(getDescription().getVersion())) {
            Logger.severe("You are not running the last stable version of SkDynmap. SkDynmap v%s is available ! Download it at %s !", result, getDescription().getWebsite());
            return result;
        }
        Logger.info("You are runing the latest version of SkDynmap !");
        return "up-to-date";
    }

    private void initConfig() throws IOException, InvalidConfigurationException, IllegalAccessException {
        saveDefaultConfig();
        if (!(getConfig().getInt("version") == 1) || !(getConfig().isSet("version"))) {
            Path configPath = Paths.get(getDataFolder().getAbsolutePath() + File.separator + "config.yml");
            Path configOldPath = Paths.get(getDataFolder().getAbsolutePath() + File.separator + "config.yml.old");
            Files.deleteIfExists(configOldPath);
            configPath.toFile().renameTo(configOldPath.toFile());
            saveDefaultConfig();
            Logger.warning("You were using an old version of the config !");
            Logger.warning("Old configuration has been saved as config.old.yml.");
            Logger.warning("A new configuration has been generated.");
            Logger.warning("Be careful ! All the config has the default values, please verify the config !");
        }
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
            storageYaml.getConfigurationSection("areas").getKeys(false).forEach(s -> {
                DynmapArea area = new DynmapArea(s);
                dynmapAreas.put(s, area);
            });
        }
        dynmapMarkers.clear();
        if (storageYaml.isSet("markers.")) {
            storageYaml.getConfigurationSection("markers").getKeys(false).forEach(s -> {
                DynmapMarker marker = new DynmapMarker(s);
                dynmapMarkers.put(s, marker);
            });
        }
    }

    public CustomYamlConfig getStorageYaml() {
        return storageYaml;
    }

    public void saveStorageYaml() {
        storageYaml = new CustomYamlConfig();
        dynmapAreas.forEach((s, dynmapArea) -> {
            storageYaml.setArea("areas." + s, dynmapArea);
        });
        dynmapMarkers.forEach((s, dynmapMarker) -> {
            storageYaml.setMarker("markers." + s, dynmapMarker);
        });
        try {
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
        final Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        dynmapCommonAPI = (DynmapCommonAPI) dynmap;
        markerAPI = dynmapCommonAPI.getMarkerAPI();
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
        int taskInterval = Config.UPDATE_INTERVAL;
        if (taskInterval > 0) {
            int renderTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    Util.renderAllAreas();
                    Util.renderAllMarkers();
                }
            }, 100, taskInterval * 20);
        }
        return true;
    }

    public boolean isDebugMode() {
        return Config.isLoaded && Config.DEBUG_MODE;
    }

    public MarkerSet getMarkerSet() {
        return markerSet;
    }

    public MarkerAPI getMarkerAPI() {
        return markerAPI;
    }
}
