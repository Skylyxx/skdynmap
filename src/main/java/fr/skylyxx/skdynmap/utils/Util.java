package fr.skylyxx.skdynmap.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.dynmap.markers.AreaMarker;

import fr.skylyxx.skdynmap.SkDynmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;


public class Util {

    private static SkDynmap skdynmap = SkDynmap.getInstance();
    public static ArrayList<AreaMarker> renderedAreas = new ArrayList<AreaMarker>();

    public static void log(String message, Level level) {
        Bukkit.getLogger().log(level, "[SkDynmap] " + message);
    }

    public static String getPrefix() {
        return ChatColor.GOLD + "[SkDynmap] ";
    }

    public static AreaStyle getDefaultStyle() {
        return new AreaStyle(skdynmap.getConfig().getString("default-style.line.color"), skdynmap.getConfig().getDouble("default-style.line.opacity"), skdynmap.getConfig().getInt("default-style.line.weight"), skdynmap.getConfig().getString("default-style.fill.color"), skdynmap.getConfig().getDouble("default-style.fill.opacity"));
    }

    public static void createArea(World world, String name, String description, Location pos1, Location pos2, AreaStyle style) {
        String markerid = world.getName() + "_" + name.replace(" ", "-");
        markerid = markerid.toLowerCase();

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

        try {
            skdynmap.getAreasConfig().save(skdynmap.getAreasFile());
            log("File areas.yml saved succesffully !", Level.INFO);
        } catch (IOException e) {
            log("Unable to save file areas.yml !", Level.SEVERE);
            e.printStackTrace();
        }

    }

    public static void deleteArea(String markerid) {
        markerid = markerid.toLowerCase();

        skdynmap.getAreasConfig().set("areas." + markerid, null);
        try {
            skdynmap.getAreasConfig().save(skdynmap.getAreasFile());
            log("File areas.yml saved succesffully !", Level.INFO);
        } catch (IOException e) {
            log("Unable to save file areas.yml !", Level.SEVERE);
            e.printStackTrace();
        }
    }

    public static void renderArea(String markerid) {
        markerid = markerid.toLowerCase();

        Location pos1 = skdynmap.getAreasConfig().getLocation("areas." + markerid + ".location.pos1");
        Location pos2 = skdynmap.getAreasConfig().getLocation("areas." + markerid + ".location.pos2");
        World world = Bukkit.getWorld(skdynmap.getAreasConfig().getString("areas." + markerid + ".location.world"));
        String name = skdynmap.getAreasConfig().getString("areas." + markerid + ".name");
        AreaStyle style = new AreaStyle(skdynmap.getAreasConfig().getString("areas." + markerid + ".style.line.color"), skdynmap.getAreasConfig().getDouble("areas." + markerid + ".style.line.opacity"), skdynmap.getAreasConfig().getInt("areas." + markerid + ".style.line.weight"), skdynmap.getAreasConfig().getString("areas." + markerid + ".style.fill.color"), skdynmap.getAreasConfig().getDouble("areas." + markerid + ".style.fill.opacity"));

        double[] x = new double[4];
        double[] z = new double[4];

        x[0] = pos1.getX(); z[0] = pos1.getZ();
        x[1] = pos1.getX(); z[1] = pos2.getZ();
        x[2] = pos2.getX(); z[2] = pos2.getZ();
        x[3] = pos2.getX(); z[3] = pos1.getZ();

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

    public static void unrenderAllAreas() {
        Iterator<AreaMarker> it = renderedAreas.iterator();
        while(it.hasNext()) {
            AreaMarker area = it.next();
            area.deleteMarker();
            it.remove();
        }
    }

    public static void renderAllAreas() {
        unrenderAllAreas();
        ConfigurationSection areasSection = skdynmap.getAreasConfig().getConfigurationSection("areas");

        if (areasSection != null) {
            areasSection.getKeys(false).forEach((n) -> renderArea(n));
        }
    }

    public static int getHexInt(String hex) {
        hex = hex.replace("#", "");
        return Integer.parseInt(hex, 16);
    }
}
