package fr.skylyxx.skdynmap;

import ch.njol.skript.util.FileUtils;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Config {

    DEBUG_MODE(false),
    UPDATE_INTERVAL(5),
    DEFAULT_MARKER_ICON("pin"),
    INFOWINDOW_WITH_DESC("<div class\"infowindow\"><span><strong>Name: </strong>%name%<br><strong>Description: </strong>%description%</div>"),
    INFOWINDOW_WITHOUT_DESC("<div class\"infowindow\"><span><strong>Name: </strong>%name%</div>"),
    DEFAULT_STYLE(new AreaStyle("#FF0000", 0.35, "#FF0000", 0.8, 3)),
    ;

    private static boolean isLoaded = false;
    private static File file;
    private static CustomYamlConfig configuration;
    private Object value;

    Config(Object value) {
        this.value = value;
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void load() {
        try {
            file = new File(SkDynmap.getINSTANCE().getDataFolder(), "config.yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            configuration = new CustomYamlConfig();
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            SkDynmap.getINSTANCE().getLogger().severe("Failed to create/load configuration file !");
            exception.printStackTrace();
        }
        for (Config cfg : Config.values()) {
            String key = cfg.toString();
            Object value = cfg.get();
            if(!Util.addDefault(configuration, key.toLowerCase(),value)) {
                cfg.set(configuration.get(key.toLowerCase()));
            }
        }
        try {
            configuration.save(file);
        } catch (IOException exception) {
            SkDynmap.getINSTANCE().getLogger().severe("Failed to save configuration file !");
            exception.printStackTrace();
        }
        isLoaded = true;
    }

    public void set(Object value) {
        this.value = value;
    }

    public <T> T get() {
        return (T) this.value;
    }
}
