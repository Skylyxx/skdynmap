package fr.skylyxx.skdynmap.utils;

import ch.njol.skript.Skript;
import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.dynmap.markers.AreaMarker;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;


public class Util {

    public static ArrayList<AreaMarker> renderedAreas = new ArrayList<AreaMarker>();
    private static SkDynmap skdynmap = SkDynmap.getInstance();
    private static YamlConfiguration targetToSave;

    public static void log(String message, Level level) {
        if (level == Level.SEVERE) {
            skdynmap.getLogger().severe(message);
        } else if (level == Level.WARNING) {
            skdynmap.getLogger().warning(message);
        } else {
            skdynmap.getLogger().info(message);
        }
    }

    public static String getPrefix() {
        return ChatColor.GOLD + "[SkDynmap] ";
    }

    public static AreaStyle getDefaultStyle() {
        return new AreaStyle(skdynmap.getConfig().getString("default-style.line.color"), skdynmap.getConfig().getDouble("default-style.line.opacity"), skdynmap.getConfig().getInt("default-style.line.weight"), skdynmap.getConfig().getString("default-style.fill.color"), skdynmap.getConfig().getDouble("default-style.fill.opacity"));
    }


    public static int getHexInt(String hex) {
        hex = hex.replace("#", "");
        return Integer.parseInt(hex, 16);
    }

    public static boolean copyConf(final YamlConfiguration sourceConfig, final String sourcePath, final YamlConfiguration targetConfig, final String targetPath) {
        try {
            final Object value = sourceConfig.get(sourcePath);
            if (!(value instanceof ConfigurationSection)) {
                //System.err.println("COPYING " + sourceConfig.getName() + ":" + sourcePath + " TO " + targetConfig.getName() + ":" + targetPath);
                targetConfig.set(targetPath, value);
                targetToSave = targetConfig;
                return true;
            }
            for (final String key : ((ConfigurationSection) value).getKeys(false)) {
                copyConf(sourceConfig, sourcePath + "." + key, targetConfig, targetPath + "." + key);
            }
            return true;
        } catch (Exception e) {
            System.err.println("ERROR COPYING " + sourceConfig.getName() + ":" + sourcePath + " TO " + targetConfig.getName() + ":" + targetPath);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean runCopy(final YamlConfiguration sourceConfig, final String sourcePath, final YamlConfiguration targetConfig, final String targetPath) {
        try {
            final ExecutorService executor = Executors.newSingleThreadExecutor();
            final Future<Boolean> result = executor.submit(() -> copyConf(sourceConfig, sourcePath, targetConfig, targetPath));
            executor.shutdown();
            final boolean botResult = result.get();
            targetToSave.save(skdynmap.getAreasFile());
            skdynmap.reloadAreasConfig();
            return botResult;
        } catch (InterruptedException | ExecutionException | IOException e) {
            return false;
        }
    }

    public static String formatMarkerID(String name, World world) {
        String markerid = world.getName() + "_" + name.replaceAll(" ", "-");
        markerid = markerid.toLowerCase();
        return markerid;
    }

    /*
        Area Management
     */

    public static void saveAreas() {
        try {
            skdynmap.getAreasConfig().save(skdynmap.getAreasFile());
            if (skdynmap.getDebugMode()) {
                log("File areas.yml saved succesffully !", Level.INFO);
            }
        } catch (IOException e) {
            if (skdynmap.getDebugMode()) {
                log("Unable to save file areas.yml !", Level.SEVERE);
            }
            e.printStackTrace();
        }
    }

    public static void createArea(World world, String name, String description, Location pos1, Location pos2, AreaStyle style) {
        String markerid = formatMarkerID(name, world);

        if (areaExist(markerid)) {
            Skript.error("You are trying to create an area but she already exist ! Aborting...");
            return;
        }

        skdynmap.getAreasConfig().set("areas." + markerid + ".name", name);
        if (description != null) {
            skdynmap.getAreasConfig().set("areas." + markerid + ".description", description);
        }
        skdynmap.getAreasConfig().set("areas." + markerid + ".location.world", world.getName());
        skdynmap.getAreasConfig().set("areas." + markerid + ".location.pos1", pos1);
        skdynmap.getAreasConfig().set("areas." + markerid + ".location.pos2", pos2);
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.fill.color", style.getFillColor());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.fill.opacity", style.getFillOpacity());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.line.color", style.getLineColor());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.line.opacity", style.getLineOpacity());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.line.weight", style.getLineWeight());

        saveAreas();

    }

    public static void createArea(AreaBuilder areaBuilder) {
        createArea(areaBuilder.getWorld(), areaBuilder.getName(), areaBuilder.getDescription(), areaBuilder.getPos1(), areaBuilder.getPos2(), areaBuilder.getStyle());
    }

    public static void deleteArea(String markerid) {
        markerid = markerid.toLowerCase();

        if (!areaExist(markerid)) {
            Skript.error("You are trying to delete an inexistant area ! Aborting...");
            return;
        }

        skdynmap.getAreasConfig().set("areas." + markerid, null);
        saveAreas();
    }

    public static void renderArea(String markerid) {
        markerid = markerid.toLowerCase();

        if (!areaExist(markerid)) {
            Skript.error("You are trying to render an inexistant area ! Aborting...");
            return;
        }

        Location pos1 = (Location) skdynmap.getAreasConfig().get("areas." + markerid + ".location.pos1");
        Location pos2 = (Location) skdynmap.getAreasConfig().get("areas." + markerid + ".location.pos2");
        World world = Bukkit.getWorld(skdynmap.getAreasConfig().getString("areas." + markerid + ".location.world"));
        String name = skdynmap.getAreasConfig().getString("areas." + markerid + ".name");
        AreaStyle style = new AreaStyle(skdynmap.getAreasConfig().getString("areas." + markerid + ".style.line.color"), skdynmap.getAreasConfig().getDouble("areas." + markerid + ".style.line.opacity"), skdynmap.getAreasConfig().getInt("areas." + markerid + ".style.line.weight"), skdynmap.getAreasConfig().getString("areas." + markerid + ".style.fill.color"), skdynmap.getAreasConfig().getDouble("areas." + markerid + ".style.fill.opacity"));

        if (pos1 == null || pos2 == null || world == null || name == null || style == null) {
            Skript.error("You are trying to rendering an area but one (ore more) of its value(s) is/are null ! Aborting...");
            return;
        }


        double[] x = new double[4];
        double[] z = new double[4];

        x[0] = pos1.getX();
        z[0] = pos1.getZ();
        x[1] = pos1.getX();
        z[1] = pos2.getZ();
        x[2] = pos2.getX();
        z[2] = pos2.getZ();
        x[3] = pos2.getX();
        z[3] = pos1.getZ();

        AreaMarker m;
        m = skdynmap.getMarkerSet().createAreaMarker(markerid, name, false, world.getName(), x, z, false);
        //m.setCornerLocations(x, z);
        m.setLabel(name);

        //style
        int fillColor = getHexInt(style.getFillColor());
        m.setFillStyle(style.getFillOpacity(), fillColor);

        int lineColor = getHexInt(style.getLineColor());
        m.setLineStyle(style.getLineWeight(), style.getLineOpacity(), Integer.parseInt(style.getLineColor().substring(1), 16));

        //popup
        if (!skdynmap.getAreasConfig().isSet("areas." + markerid + ".description")) {
            String desc = skdynmap.DEF_INFOWINDOW_WITHOUTDESC;
            desc = desc.replaceAll("%name%", name);
            m.setDescription(desc);
        } else {
            String description = skdynmap.getAreasConfig().getString("areas." + markerid + ".description");
            String desc = skdynmap.DEF_INFOWINDOW_WITHDESC;
            desc = desc.replaceAll("%name%", name);
            desc = desc.replaceAll("%description%", description);
            m.setDescription(desc);
        }

        renderedAreas.add(m);

    }

    public static void unRenderAllAreas() {
        Iterator<AreaMarker> it = renderedAreas.iterator();
        while (it.hasNext()) {
            AreaMarker area = it.next();
            area.deleteMarker();
            it.remove();
        }
    }

    public static void renderAllAreas() {
        unRenderAllAreas();
        ConfigurationSection areasSection = skdynmap.getAreasConfig().getConfigurationSection("areas");

        if (areasSection != null) {
            areasSection.getKeys(false).forEach((n) -> renderArea(n));
        }
    }

    @Nullable
    public static DynmapArea getArea(String name, World world) {
        if (Util.areaExist(name, world)) {
            return new DynmapArea(name, world);
        } else {
            return null;
        }
    }

    public static boolean areaExist(@Nullable String name, @Nullable World world) {
        if (name == null || world == null) {
            return false;
        }
        String markerid = world.getName() + "_" + name.replaceAll(" ", "-");
        markerid = markerid.toLowerCase();

        return areaExist(markerid);
    }

    public static boolean areaExist(String markerid) {
        if (skdynmap.getAreasConfig().get("areas." + markerid) != null) {
            return true;
        }
        return false;
    }

    public static boolean areaExist(DynmapArea area) {
        try {
            String name = area.getName();
            World world = area.getWorld();
            return areaExist(name, world);
        } catch (NullPointerException err) {
            return false;
        }
    }

    public static void setAreaStyle(DynmapArea area, AreaStyle style) {
        if (!areaExist(area)) {
            log("You are trying to change the style of an inexistant area ! Aborting...", Level.SEVERE);
            return;
        }
        String name = area.getName();
        World world = area.getWorld();

        String markerid = world.getName() + "_" + name.replaceAll(" ", "-");
        markerid = markerid.toLowerCase();

        skdynmap.getAreasConfig().set("areas." + markerid + ".style.fill.color", style.getFillColor());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.fill.opacity", style.getFillOpacity());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.line.color", style.getLineColor());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.line.opacity", style.getLineOpacity());
        skdynmap.getAreasConfig().set("areas." + markerid + ".style.line.weight", style.getLineWeight());

        saveAreas();
    }

    @Nullable
    public static DynmapArea[] getAllAreas() {
        ArrayList<DynmapArea> arrayList = new ArrayList<DynmapArea>();
        Object configurationSection = skdynmap.getAreasConfig().get("areas");
        if(configurationSection instanceof ConfigurationSection) {
            for (final String key : ((ConfigurationSection) configurationSection).getKeys(false)) {
                String name = skdynmap.getAreasConfig().getString("areas." + key + ".name");
                World world = Bukkit.getWorld(skdynmap.getAreasConfig().getString("areas." + key + ".location.world"));
                DynmapArea currentArea = getArea(name, world);
                arrayList.add(currentArea);
                Util.log(key + " added", Level.INFO);
                Util.log(name, Level.INFO);
                Util.log(world.getName(), Level.INFO);
            }
        }
        Util.log("Size: " + arrayList.size(), Level.INFO);
        DynmapArea[] areas = new DynmapArea[arrayList.size()];
        areas = arrayList.toArray(areas);
        if(areas == null) {
            Util.log("Areas null",Level.INFO);
        }
        return areas;
    }
}
