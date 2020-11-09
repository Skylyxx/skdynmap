package fr.skylyxx.skdynmap;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.google.common.io.Files;
import fr.skylyxx.skdynmap.commands.CMDSkDynmap;
import fr.skylyxx.skdynmap.utils.Metrics;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
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
import java.util.logging.Level;

public class SkDynmap extends JavaPlugin {

    public static String DEF_INFOWINDOW_WITHDESC;
    public static String DEF_INFOWINDOW_WITHOUTDESC;
    private static SkDynmap INSTANCE;
    private PluginManager pm;
    private SkriptAddon addon;
    private File areasFile;
    private YamlConfiguration areasConfig;
    private Plugin dynmap;
    private DynmapCommonAPI api;
    private MarkerAPI markerapi;
    private MarkerSet set;

    public static SkDynmap getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        pm = Bukkit.getPluginManager();

        Metrics metrics = new Metrics(this, 9273);

        final Plugin SKRIPT = pm.getPlugin("Skript");
        if (SKRIPT != null && SKRIPT.isEnabled() == true && Skript.isAcceptRegistrations()) {
            final Plugin DYNMAP = pm.getPlugin("dynmap");
            if (DYNMAP != null && DYNMAP.isEnabled() == true) {

                addon = Skript.registerAddon(this);

                //Check for updates
                try {
                    checkForUpdates();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Skript stuff registration
                loadSkript();

                // Loading config files
                initConfig();

                //Init Dynmap layer
                initDynmap();

                // Registering command
                this.getCommand("skdynmap").setExecutor(new CMDSkDynmap());

                // BETA Warning
                if (getDescription().getVersion().contains("beta")) {
                    Util.log("This is a BETA build of SkDynmap, things may not work as expected ! Please report bugs on Github !", Level.WARNING);
                    Util.log(getDescription().getWebsite(), Level.WARNING);
                }
            } else {
                Util.log("Dynmap dependency was not found ! Disabling SkDynmap !", Level.SEVERE);
                pm.disablePlugin(this);
                return;
            }
        } else {
            Util.log("Skript dependency was not found ! Disabling SkDynmap", Level.SEVERE);
            pm.disablePlugin(this);
            return;
        }
        Util.log("Successfully enabled SkDynmap v" + getDescription().getVersion(), Level.INFO);
    }

    // For getting result, using code from Olyno's Skent: https://github.com/Olyno/skent/blob/master/src/main/java/com/olyno/skent/skript/expressions/ExprContentFromURL.java#L59
    @Nullable
    public String checkForUpdates() throws IOException {
        URL url = new URL("https://pastebin.com/ZECi01d9");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        bufferedReader.close();
        String result = stringBuilder.toString().trim().split("<textarea class=\"textarea\">")[1].split("</textarea>")[0];
        if (!result.equalsIgnoreCase(getDescription().getVersion())) {
            Util.log("You are running an old version of SkDynmap, SkDynmap v" + result + " is available ! Download it at " + getDescription().getWebsite() + "/releases !", Level.SEVERE);
            return result;
        }
        Util.log("You are runing the latest version of SkDynmap !", Level.INFO);
        return "up-to-date";
    }

    private void initConfig() {
        Util.log("Loading config files...", Level.INFO);
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            Util.log("Unable to find config.yml ! Generating file...", Level.WARNING);
        } else {
            String version = getConfig().getString("version");
            if (version == null || !version.equalsIgnoreCase(getDescription().getVersion())) {
                Util.log("You had an old version of config.yml, file has be regenerated. You might have to remake your changes. Old config.yml is available as config.yml.backup !", Level.SEVERE);
                File backupConfig = new File(getDataFolder(), "config.yml.backup");
                if (backupConfig.exists()) {
                    backupConfig.delete();
                }
                try {
                    Files.copy(configFile, backupConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                configFile.delete();
                saveDefaultConfig();

            } else {
                Util.log("Config is up to date !", Level.INFO);
            }
        }
        saveDefaultConfig();

        DEF_INFOWINDOW_WITHDESC = getConfig().getString("info-window.with-desc");
        DEF_INFOWINDOW_WITHOUTDESC = getConfig().getString("info-window.without-desc");

        areasFile = new File(getDataFolder(), "areas.yml");
        if (!areasFile.exists()) {
            areasFile.getParentFile().mkdirs();
            saveResource("areas.yml", false);
        }

        areasConfig = new YamlConfiguration();
        try {
            areasConfig.load(areasFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            Util.log("Error while loading areas.yml", Level.SEVERE);
            e.printStackTrace();
        }
    }

    public void reloadAreasConfig() {
        areasConfig = YamlConfiguration.loadConfiguration(areasFile);
    }

    public void reloadAllConfigs() {
        this.reloadConfig();
        areasConfig = YamlConfiguration.loadConfiguration(areasFile);

        DEF_INFOWINDOW_WITHDESC = getConfig().getString("info-window.with-desc");
        DEF_INFOWINDOW_WITHOUTDESC = getConfig().getString("info-window.without-desc");
    }

    public YamlConfiguration getAreasConfig() {
        return areasConfig;
    }

    public File getAreasFile() {
        return areasFile;
    }

    public MarkerSet getMarkerSet() {
        return set;
    }

    private void initDynmap() {
        dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        api = (DynmapCommonAPI) dynmap;
        markerapi = api.getMarkerAPI();

        if (markerapi == null) {
            Util.log("Error loading dynmap MarkerAPI !", Level.SEVERE);
            return;
        }
        Util.log("MarkerAPI loaded successfully.", Level.INFO);

        set = markerapi.getMarkerSet("skdynmap.markerset");
        if (set == null) {
            set = markerapi.createMarkerSet("skdynmap.markerset", "SkDynmap", null, false);
        } else {
            set.setMarkerSetLabel("SkDynmap");
        }

        set.setMinZoom(0);
        set.setLayerPriority(10);
        set.setHideByDefault(false);

        int renderTaskInterval = getConfig().getInt("update-interval");
        if (renderTaskInterval > 0) {
            int renderTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    reloadAreasConfig();
                    Util.renderAllAreas();
                }
            }, 100, renderTaskInterval * 20);
        } else {
            Util.log("Areas will not be rendered automatically because it is disable in the configuration.", Level.INFO);
            Util.log("To enable it, set \"update-interval\" parameter to an number equals or greater than 1.", Level.INFO);
        }
    }

    private void loadSkript() {
        try {
            addon.loadClasses("fr.skylyxx.skdynmap.skript");
        } catch (IOException e) {
            Util.log("Error while loading  SkDynmap's syntaxes !", Level.SEVERE);
            e.printStackTrace();
        }
    }
}
