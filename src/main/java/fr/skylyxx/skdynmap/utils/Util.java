package fr.skylyxx.skdynmap.utils;

import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.CustomYamlConfig;
import fr.skylyxx.skdynmap.Logger;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.GenericMarker;

import java.util.ArrayList;
import java.util.List;

public class Util {

    final static List<Pair<Integer, Integer>> coordinateCouple = new ArrayList<>();
    private static final SkDynmap skDynmap = SkDynmap.getINSTANCE();
    private static final ArrayList<AreaMarker> renderedAreas = new ArrayList<AreaMarker>();

    /*
        OTHER
     */
    public static boolean addDefault(YamlConfiguration config, String path, Object o) {
        if (config == null) return true;
        if (!config.isSet(path)) {
            if (config instanceof CustomYamlConfig) {
                if (o instanceof AreaStyle)
                    ((CustomYamlConfig) config).setStyle(path, ((AreaStyle) o));
                else if (o instanceof DynmapArea)
                    ((CustomYamlConfig) config).setArea(path, ((DynmapArea) o));
                else if (o instanceof DynmapMarker)
                    ((CustomYamlConfig) config).setMarker(path, ((DynmapMarker) o));
                else
                    config.set(path, o);
            } else {
                config.set(path, o);
            }
            return true;
        }
        return false;
    }

    /*
        CHUNKS CORNERS
     */

    public static int hexToInt(String hex) {
        hex = hex.replace("#", "");
        return Integer.parseInt(hex, 16);
    }

    public static List<Location> getCorners(final List<Chunk> chunks) {
        final List<Location> output = new ArrayList<>();
        if (chunks.size() == 0)
            return output;
        coordinateCouple.clear();
        coordinateCouple.add(new Pair<>(-1, 0)); // left
        coordinateCouple.add(new Pair<>(0, -1)); // top
        coordinateCouple.add(new Pair<>(1, 0));  // right
        coordinateCouple.add(new Pair<>(0, 1));  // down
        int startIndex = 0;
        final List<Chunk> viewedChunks = new ArrayList<>();
        for (final Chunk chunk : chunks) {
            if (viewedChunks.contains(chunk))
                continue;
            final Pair<List<Location>, Pair<List<Chunk>, Integer>> intermediateResult = cornerRecursive(chunk, chunks, viewedChunks, startIndex);
            intermediateResult.getFirst().stream().filter(location -> !output.contains(location)).forEach(output::add);
            startIndex = intermediateResult.getSecond().getSecond();
        }
        return output;
    }

    private static Pair<List<Location>, Pair<List<Chunk>, Integer>> cornerRecursive(final Chunk seeing, final List<Chunk> toSee, final List<Chunk> viewed, final int startAt) {
        // System.out.println("blabla: " + seeing.getX() + " / " + seeing.getZ());
        final List<Location> locations = new ArrayList<>();
        viewed.add(seeing);
        int start = startAt;
        // System.out.println(startAt);
        for (int index = 0; index < coordinateCouple.size(); index++) {
            start %= coordinateCouple.size();
            final Pair<Integer, Integer> relativeCoordinate = coordinateCouple.get(start);
            final Chunk chunk = seeing.getWorld().getChunkAt(seeing.getX() + relativeCoordinate.getFirst(), seeing.getZ() + relativeCoordinate.getSecond());
            // System.out.println(viewed);
            if (viewed.contains(chunk)) {
                start++;
                continue;
            }
            if (toSee.contains(chunk)) {
                final Pair<List<Location>, Pair<List<Chunk>, Integer>> result = cornerRecursive(chunk, toSee, viewed, start + 3);
                locations.addAll(result.getFirst());
                viewed.addAll(result.getSecond().getFirst());
                start++;
                continue;
            }
            if (relativeCoordinate.getFirst() == -1) {
                locations.add(seeing.getBlock(0, 0, 15).getLocation().add(0, 0, 1));
                locations.add(seeing.getBlock(0, 0, 0).getLocation().add(0, 0, 0));
            } else if (relativeCoordinate.getFirst() == 1) {
                locations.add(seeing.getBlock(15, 0, 0).getLocation().add(1, 0, 0));
                locations.add(seeing.getBlock(15, 0, 15).getLocation().add(1, 0, 1));
            } else if (relativeCoordinate.getSecond() == -1) {
                locations.add(seeing.getBlock(0, 0, 0).getLocation().add(0, 0, 0));
                locations.add(seeing.getBlock(15, 0, 0).getLocation().add(1, 0, 0));
            } else {
                locations.add(seeing.getBlock(0, 0, 15).getLocation().add(0, 0, 1));
                locations.add(seeing.getBlock(15, 0, 15).getLocation().add(1, 0, 1));
            }
            start++;
        }
        return new Pair<>(locations, new Pair<>(viewed, start));
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
            desc = Config.INFOWINDOW_WITHOUT_DESC.get();
            desc = desc.replaceAll("%name%", name);
        } else {
            desc = Config.INFOWINDOW_WITH_DESC.get();
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
