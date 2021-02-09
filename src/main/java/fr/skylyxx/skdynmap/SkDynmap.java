package fr.skylyxx.skdynmap;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.skylyxx.skdynmap.commands.CmdSkDynmap;
import fr.skylyxx.skdynmap.utils.Util;
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

public class SkDynmap extends JavaPlugin {

    private static SkDynmap INSTANCE;
    public SkriptAddon addon;

    public File storageFile;
    public CustomYamlConfig storageYaml;

    public DynmapCommonAPI dynmapCommonAPI;
    public MarkerAPI markerAPI;
    public MarkerSet markerSet;

    public static SkDynmap getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        final PluginManager pm = Bukkit.getPluginManager();
        final Plugin SKRIPT = pm.getPlugin("Skript");
        final Plugin DYNMAP = pm.getPlugin("dynmap");
        if (SKRIPT == null || !SKRIPT.isEnabled()) {
            getLogger().severe("Skript dependency was not found ! Disabling...");
            pm.disablePlugin(this);
            return;
        }
        if (DYNMAP == null || !DYNMAP.isEnabled()) {
            getLogger().severe("Dynmap dependency was not found ! Disabling...");
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

        PluginCommand skDynmapCommand = getCommand("skdynmap");
        skDynmapCommand.setExecutor(new CmdSkDynmap());
        skDynmapCommand.setPermission("skdynmap.use");
        skDynmapCommand.setPermissionMessage("&cSorry but you don't have the required permission !");

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
            getLogger().severe("You are not running the last stable version of SkDynmap. SkDynmap v" + result + " is available ! Download it at " + getDescription().getWebsite() + "/releases !");
            return result;
        }
        getLogger().info("You are runing the latest version of SkDynmap !");
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
            getLogger().warning("You where using an old version of the config !");
            getLogger().warning("Old configuration has been saved as config.old.yml.");
            getLogger().warning("A new configuration has been generated.");
            getLogger().warning("Be careful ! All the config has the default values, please verify the config !");
        }
        Config.load();

        storageFile = new File(getDataFolder() + File.separator + "storage.yml");
        if (!storageFile.exists()) {
            storageFile.getParentFile().mkdirs();
            storageFile.createNewFile();
        }
        reloadStorageConfig();
    }


    public void reloadStorageConfig() throws IOException, InvalidConfigurationException {
        storageYaml = new CustomYamlConfig(storageFile);
        storageYaml.load(storageFile);
    }

    public CustomYamlConfig getStorageYaml() {
        return storageYaml;
    }

    public void saveStorageYaml() throws IOException {
        storageYaml.save(storageFile);
        if(isDebugMode()) {
            getLogger().info("File storage.yml was saved successfully");
        }
    }

    public void reloadSkDynmapConfig() throws IOException, InvalidConfigurationException, IllegalAccessException {
        reloadConfig();
        Config.load();
        reloadStorageConfig();
        if (isDebugMode()) {
            getLogger().info("Config has been reloaded !");
        }
    }

    private boolean loadSkript() {
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("fr.skylyxx.skdynmap.skript");
        } catch (IOException e) {
            getLogger().severe("Unable to load SkDynmap's syntaxes ! Disabling...");
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
            getLogger().severe("There was an error while loading MarkerAPI ! Disabling...");
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
}
