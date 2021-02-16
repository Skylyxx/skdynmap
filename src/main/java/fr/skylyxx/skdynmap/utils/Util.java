package fr.skylyxx.skdynmap.utils;

import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.Logger;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.Location;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.GenericMarker;

import java.util.ArrayList;

public class Util {

    private static final SkDynmap skDynmap = SkDynmap.getINSTANCE();

    private static final ArrayList<AreaMarker> renderedAreas = new ArrayList<AreaMarker>();
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
        if (!areaExist(id)) {
            return null;
        }
        return new DynmapArea(id);
    }

    public static boolean areaExist(String id) {
        return skDynmap.dynmapAreas.containsKey(id);
    }

    public static boolean areaExist(DynmapArea area) {
        if (area == null) {
            return false;
        }
        return skDynmap.dynmapAreas.containsKey(area.getId());
    }

    public static DynmapArea[] getAllAreas() {
        return skDynmap.dynmapAreas.values().toArray(new DynmapArea[0]);
    }

    public static void renderArea(DynmapArea area) {
        unRenderArea(area);
        if (!areaExist(area)) {
            Logger.warning("You are trying to render an instant area ! (%s)", true, area.getId());
            return;
        }
        String name = area.getName();
        String description = area.getDescription();
        Location[] locations = area.getLocations();
        AreaStyle areaStyle = area.getAreaStyle();

        if (name == null || locations == null || areaStyle == null) {
            Logger.warning("You are trying to render an area ! (%s) but one or more of its values is/are null", true, area.getId());
            return;
        }

        double[] x;
        double[] z;

        if (locations.length < 2) {
            Logger.warning("At least 2 locations are required to render areas ! Aborting...");
            return;
        } else if (locations.length == 2) {
            x = new double[4];
            z = new double[4];
            int index = 0;
            for (int xIndex = 0; xIndex < 2; xIndex++) {
                for (int zIndex = 0; zIndex < 2; zIndex++) {
                    x[index] = locations[xIndex].getX();
                    z[index] = locations[zIndex].getZ();
                    index++;
                }
            }
        } else {
            x = new double[locations.length];
            z = new double[locations.length];
            for (int index = 0; index < locations.length; index++) {
                x[index] = locations[index].getX();
                z[index] = locations[index].getZ();
            }
        }

        final AreaMarker m = skDynmap.getMarkerSet().createAreaMarker(area.getId(), name, false, locations[0].getWorld().getName(), x, z, false);

        m.setLabel(name);
        m.setFillStyle(areaStyle.getFillOpacity(), hexToInt(areaStyle.getFillColor()));
        m.setLineStyle(areaStyle.getLineWeight(), areaStyle.getLineOpacity(), hexToInt(areaStyle.getLineColor()));

        String desc;
        if (description == null || description.trim().isEmpty()) {
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
        skDynmap.getMarkerSet().getAreaMarkers().stream()
                .filter(areaMarker -> areaMarker.getMarkerID().equalsIgnoreCase(area.getId()))
                .forEach(GenericMarker::deleteMarker);
    }

    public static void renderAllAreas() {
        unRenderAllAreas();
        for (DynmapArea area : getAllAreas()) {
            renderArea(area);
        }
    }

    public static void unRenderAllAreas() {
        skDynmap.getMarkerSet().getAreaMarkers().forEach(GenericMarker::deleteMarker);
    }

    public static boolean isRendered(DynmapArea area) {
        return skDynmap.getMarkerSet().findAreaMarker(area.getId()) != null;
    }

    /*
        MARKERS
     */

    public static DynmapMarker getMarkerByID(String id) {
        if (!markerExist(id)) {
            return null;
        }
        return new DynmapMarker(id);
    }

    public static boolean markerExist(String id) {
        return skDynmap.dynmapMarkers.containsKey(id);
    }

    public static boolean markerExist(DynmapMarker marker) {
        if (marker == null) {
            return false;
        }
        return skDynmap.dynmapMarkers.containsKey(marker.getId());
    }

    public static DynmapMarker[] getAllMarkers() {

        return skDynmap.dynmapMarkers.values().toArray(new DynmapMarker[0]);
    }

    public static void unRenderMarker(DynmapMarker dynmapMarker) {
        skDynmap.getMarkerSet().getMarkers().stream()
                .filter(marker -> marker.getMarkerID().equalsIgnoreCase(dynmapMarker.getId()))
                .forEach(GenericMarker::deleteMarker);
    }

    public static void renderAllMarkers() {
        unRenderAllMarkers();
        for (DynmapMarker marker : getAllMarkers()) {
            renderMarker(marker);
        }
    }

    public static void renderMarker(DynmapMarker marker) {
        if (!markerExist(marker)) {
            return;
        }
        SkDynmap.getINSTANCE().getMarkerSet().createMarker(
                marker.getId(),
                marker.getName(),
                marker.getLocation().getWorld().getName(),
                marker.getLocation().getX(),
                marker.getLocation().getY(),
                marker.getLocation().getZ(),
                marker.getMarkerIcon(),
                false
        );
    }

    public static void unRenderAllMarkers() {
        skDynmap.getMarkerSet().getMarkers().forEach(GenericMarker::deleteMarker);
    }

}
