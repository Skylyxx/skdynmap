package fr.skylyxx.skdynmap;

import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomYamlConfig extends YamlConfiguration {

    public DynmapArea getArea(String path) {
        if (!isSet(path)) {
            return null;
        }
        String[] paths = path.split("\\.");
        String id = paths[paths.length - 1];
        String name = getString(path + ".name");
        String desc = getString(path + ".description");
        AreaStyle areaStyle = getStyle(path + ".style");

        List<?> locationList = getList(path + ".locations");
        if (locationList == null) {
            return new DynmapArea(id, name, desc, null, areaStyle);
        }
        Location[] locations = new Location[locationList.size()];
        locations = locationList.toArray(locations);
        return new DynmapArea(id, name, desc, locations, areaStyle);
    }

    public void setArea(String path, DynmapArea area) {
        set(path + ".name", area.getName());
        set(path + ".description", area.getDescription());
        set(path + ".locations", area.getLocations());
        setStyle(path + ".style", area.getAreaStyle());
    }

    public AreaStyle getStyle(String path) {
        String fillColor = getString(path + ".fill.color", Config.DEFAULT_STYLE.<AreaStyle>get().getFillColor());
        double fillOpacity = getDouble(path + ".fill.opacity", Config.DEFAULT_STYLE.<AreaStyle>get().getFillOpacity());
        String lineColor = getString(path + ".line.color", Config.DEFAULT_STYLE.<AreaStyle>get().getLineColor());
        double lineOpacity = getDouble(path + ".line.opacity", Config.DEFAULT_STYLE.<AreaStyle>get().getLineOpacity());
        int lineWeight = getInt(path + ".line.weight", Config.DEFAULT_STYLE.<AreaStyle>get().getLineWeight());
        return new AreaStyle(fillColor, fillOpacity, lineColor, lineOpacity, lineWeight);
    }

    public void setStyle(String path, AreaStyle areaStyle) {
        set(path + ".fill.color", areaStyle.getFillColor());
        set(path + ".fill.opacity", areaStyle.getFillOpacity());
        set(path + ".line.color", areaStyle.getLineColor());
        set(path + ".line.opacity", areaStyle.getLineOpacity());
        set(path + ".line.weight", areaStyle.getLineWeight());
    }

    public DynmapMarker getMarker(String path) {
        if (!isSet(path)) {
            return null;
        }
        String[] paths = path.split("\\.");
        String id = paths[paths.length - 1];
        String name = getString(path + ".name");
        Location location = (Location) get(path + ".location");
        String icon = getString(path + ".icon");
        return new DynmapMarker(id, name, location, icon);
    }

    public void setMarker(String path, DynmapMarker marker) {
        set(path + ".name", marker.getName());
        set(path + ".location", marker.getLocation());
        set(path + ".icon", marker.getMarkerIcon().getMarkerIconID());
    }

    public void rename(String oldPath, String newPath) throws ExecutionException, InterruptedException, IOException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<Boolean> result = executor.submit(() -> copy(oldPath, newPath));
        executor.shutdown();
        final boolean botResult = result.get();
        set(oldPath, null);
    }

    public boolean copy(String source, String target) {
        try {
            final Object val = get(source);
            if (!(val instanceof ConfigurationSection)) {
                set(target, val);
                return true;
            }
            for (final String key : ((ConfigurationSection) val).getKeys(false)) {
                copy(source + "." + key, target + "." + key);
            }
            return true;
        } catch (Exception e) {
            Logger.severe("Error copying %s to %s", source, target);
            e.printStackTrace();
            return false;
        }
    }

}
