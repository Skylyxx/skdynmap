package fr.skylyxx.skdynmap;

import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Config {

    public static boolean isLoaded;
    public static boolean DEBUG_MODE;
    public static int UPDATE_INTERVAL;
    public static String DEFAULT_MARKER_ICON;
    public static InfoWindow INFO_WINDOW;
    public static AreaStyle DEFAULT_STYLE;
    private static final FileConfiguration configFile = SkDynmap.getINSTANCE().getConfig();

    public static void load() throws IllegalAccessException {
        for (Field field : Config.class.getFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getName().equalsIgnoreCase("isloaded")) {
                continue;
            }
            if (field.getName().equalsIgnoreCase("info_window")) {
                String withDesc = configFile.getString("info_window.with_desc");
                String withoutDesc = configFile.getString("info_window.without_desc");
                field.set(null, new InfoWindow(withDesc, withoutDesc));
            } else if (field.getName().equalsIgnoreCase("default_style")) {
                AreaStyle style = new AreaStyle(
                        configFile.getString("default_style.fill.color"),
                        configFile.getDouble("default_style.fill.opacity"),
                        configFile.getString("default_style.line.color"),
                        configFile.getDouble("default_style.line.opacity"),
                        configFile.getInt("default_style.line.weight")
                );
                field.set(null, style);
            } else {
                field.set(null, configFile.get(field.getName().toLowerCase()));
            }
            isLoaded = true;
        }
    }

    public static class InfoWindow {
        public static String WITH_DESC;
        public static String WITHOUT_DESC;

        public InfoWindow(String withDesc, String withoutDesc) {
            WITH_DESC = withDesc;
            WITHOUT_DESC = withoutDesc;
        }
    }

}
