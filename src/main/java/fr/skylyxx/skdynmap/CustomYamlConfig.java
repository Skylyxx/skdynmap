package fr.skylyxx.skdynmap;

import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomYamlConfig extends YamlConfiguration {

    private File file;

    public CustomYamlConfig(File file) {
        this.file = file;
    }

    public DynmapArea getArea(String path) {
        if(!isSet(path)) {
            return null;
        }
        String[] paths = path.split("\\.");
        String id = paths[paths.length - 1];
        String name = getString(path + ".name");
        String desc = getString(path + ".description");
        AreaStyle areaStyle = getStyle(path + ".style");

        List<?> locationList = getList(path + ".locations");
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
        String fillColor = getString(path + ".fill.color", Config.DEFAULT_STYLE.getFillColor());
        double fillOpacity = getDouble(path + ".fill.opacity", Config.DEFAULT_STYLE.getFillOpacity());
        String lineColor = getString(path + ".line.color", Config.DEFAULT_STYLE.getLineColor());
        double lineOpacity = getDouble(path + ".line.opacity", Config.DEFAULT_STYLE.getLineOpacity());
        int lineWeight = getInt(path + ".line.weight", Config.DEFAULT_STYLE.getLineWeight());
        return new AreaStyle(fillColor, fillOpacity, lineColor, lineOpacity, lineWeight);
    }

    public void setStyle(String path, AreaStyle areaStyle) {
        set(path + ".fill.color", areaStyle.getFillColor());
        set(path + ".fill.opacity", areaStyle.getFillOpacity());
        set(path + ".line.color", areaStyle.getLineColor());
        set(path + ".line.opacity", areaStyle.getLineOpacity());
        set(path + ".line.weight", areaStyle.getLineWeight());
    }

    public void rename(String oldPath, String newPath) throws ExecutionException, InterruptedException, IOException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<Boolean> result = executor.submit(() -> copy(oldPath, newPath));
        executor.shutdown();
        final boolean botResult = result.get();
        set(oldPath, null);
        save(file);
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
            SkDynmap.getINSTANCE().getLogger().severe("Error copying " + source + " to " + target);
            e.printStackTrace();
            return false;
        }
    }

}
