package fr.skylyxx.skdynmap.utils;

import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.dynmap.markers.AreaMarker;

import java.util.ArrayList;

public class Util {

    private static SkDynmap skDynmap = SkDynmap.getINSTANCE();

    private static ArrayList<AreaMarker> renderedAreas = new ArrayList<AreaMarker>();
    /*
        OTHER
     */

    public static int hexToInt(String hex) {
        hex = hex.replace("#", "");
        return Integer.parseInt(hex, 16);
    }

    /*
        AREA
     */

    public static DynmapArea getAreaByID(String id) {
        return new DynmapArea(id);
    }

    public static boolean areaExist(DynmapArea area) {
        return skDynmap.getStorageYaml().isSet("areas." + area.getId());
    }

    public static DynmapArea[] getAllAreas() {
        ArrayList<DynmapArea> arrayList = new ArrayList<>();
        ConfigurationSection configurationSection = skDynmap.getStorageYaml().getConfigurationSection("areas");
        if (configurationSection == null) {
            return new DynmapArea[0];
        }
        configurationSection.getKeys(false).forEach((n) -> arrayList.add(getAreaByID(n)));
        DynmapArea[] areas = new DynmapArea[arrayList.size()];
        areas = arrayList.toArray(areas);
        return areas;
    }

    public static void renderArea(DynmapArea area) {
        unRenderArea(area);
        if (!areaExist(area)) {
            if (skDynmap.isDebugMode()) {
                skDynmap.getLogger().warning("You are trying to render an instant area ! (" + area.getId() + ")");
            }
            return;
        }
        String name = area.getName();
        String description = area.getDescription();
        Location[] locations = area.getLocations();
        AreaStyle areaStyle = area.getAreaStyle();

        if (name == null || locations == null || areaStyle == null) {
            if (skDynmap.isDebugMode()) {
                skDynmap.getLogger().warning("You are trying to render an area ! (" + area.getId() + ") but one or more of its values is/are null");
            }
            return;
        }

        double[] x;
        double[] z;

        if (locations.length < 2) {
            skDynmap.getLogger().warning("At least 2 locations are required to render areas ! Aborting...");
            return;
        } else if (locations.length == 2) {
            x = new double[4];
            z = new double[4];
            x[0] = locations[0].getX();
            z[0] = locations[0].getZ();
            x[1] = locations[0].getX();
            z[1] = locations[1].getZ();
            x[2] = locations[1].getX();
            z[2] = locations[1].getZ();
            x[3] = locations[1].getX();
            z[3] = locations[0].getZ();
        } else {
            x = new double[locations.length];
            z = new double[locations.length];
            int index = 0;
            for (Location pos : locations) {
                x[index] = pos.getX();
                z[index] = pos.getZ();
                index++;
            }
        }

        AreaMarker m;
        m = skDynmap.getMarkerSet().createAreaMarker(area.getId(), name, false, locations[0].getWorld().getName(), x, z, false);
        m.setLabel(name);

        int fillColor = hexToInt(areaStyle.getFillColor());
        m.setFillStyle(areaStyle.getFillOpacity(), fillColor);
        int lineColor = hexToInt(areaStyle.getLineColor());
        m.setLineStyle(areaStyle.getLineWeight(), areaStyle.getLineOpacity(), lineColor);

        String desc;
        if (description == null || description.isBlank()) {
            desc = Config.INFO_WINDOW.WITHOUT_DESC;
            desc = desc.replaceAll("%name%", name);
        } else {
            desc = Config.INFO_WINDOW.WITH_DESC;
            desc = desc.replaceAll("%name%", name);
            desc = desc.replaceAll("%description%", description);
        }
        m.setDescription(desc);
        renderedAreas.add(m);
    }

    public static void unRenderArea(DynmapArea area) {
       skDynmap.getMarkerSet().getAreaMarkers().forEach(areaMarker -> {
          if(areaMarker.getMarkerID().equalsIgnoreCase(area.getId())) {
              areaMarker.deleteMarker();
              return;
          }
       });
    }

    public static void renderAllAreas() {
        unRenderAll();
        for (DynmapArea area : getAllAreas()) {
            renderArea(area);
        }
    }

    public static void unRenderAll() {
        skDynmap.getMarkerSet().getAreaMarkers().forEach(areaMarker -> areaMarker.deleteMarker());
        skDynmap.getMarkerSet().getMarkers().forEach(marker -> marker.deleteMarker());
    }

    public static boolean isRendered(DynmapArea area) {
        return skDynmap.getMarkerSet().findAreaMarker(area.getId()) != null;
    }

}
