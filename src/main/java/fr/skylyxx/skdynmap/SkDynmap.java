package fr.skylyxx.skdynmap;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import fr.skylyxx.skdynmap.commands.CMDSkDynmap;
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SkDynmap extends JavaPlugin {

    private static SkDynmap INSTANCE;
    private PluginManager pm;
    private SkriptAddon addon;

    private File areasFile;
    private YamlConfiguration areasConfig;

    private Plugin dynmap;
    private DynmapCommonAPI api;
    private MarkerAPI markerapi;
    private MarkerSet set;
    public static String DEF_INFOWINDOW_WITHDESC;
    public static String DEF_INFOWINDOW_WITHOUTDESC;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.pm = Bukkit.getPluginManager();

        final Plugin SKRIPT = pm.getPlugin("Skript");
        if (SKRIPT != null && SKRIPT.isEnabled() == true && Skript.isAcceptRegistrations()) {
            addon = Skript.registerAddon(this);

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
                Util.log("This is a BETA build of SkDynmap, things may not work as expected ! Please report bugs on Gitub !", Level.WARNING);
                Util.log(getDescription().getWebsite(), Level.WARNING);
            }
        } else {
            Util.log("Skript dependency was not found ! Disabling SkDynmap", Level.SEVERE);
            pm.disablePlugin(this);
            return;
        }
        Util.log("Successfully enabled SkDynmap v" + getDescription().getVersion(), Level.INFO);
    }

    private void initConfig() {
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

    public void reloadSkDynmapConfig() {
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
        api = (DynmapCommonAPI)dynmap;
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
            Util.log("Error while enable SkDynmap's syntaxes !", Level.SEVERE);
            e.printStackTrace();
        }
    }

    public static SkDynmap getInstance() {
        return INSTANCE;
    }
}
