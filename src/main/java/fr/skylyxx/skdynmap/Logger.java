package fr.skylyxx.skdynmap;

public class Logger {

    static java.util.logging.Logger logger = SkDynmap.getINSTANCE().getLogger();

    public static void severe(String msg, String... args) {
        for (String arg : args) {
            msg = msg.replace("%s", arg);
        }
        logger.severe(msg);
    }
    public static void severe(String msg, boolean debug, String... args) {
        if (debug) {
            if(SkDynmap.getINSTANCE().isDebugMode()) {
                info(msg, args);
            }
            return;
        }
        info(msg, args);
    }

    public static void warning(String msg, String... args) {
        for (String arg : args) {
            msg = msg.replace("%s", arg);
        }
        logger.warning(msg);
    }
    public static void warning(String msg, boolean debug, String... args) {
        if (debug) {
            if(SkDynmap.getINSTANCE().isDebugMode()) {
                warning(msg, args);
            }
            return;
        }
        warning(msg, args);
    }

    public static void info(String msg, String... args) {
        for (String arg : args) {
            msg = msg.replace("%s", arg);
        }
        logger.info(msg);
    }
    public static void info(String msg, boolean debug, String... args) {
        if (debug) {
            if(SkDynmap.getINSTANCE().isDebugMode()) {
                info(msg, args);
            }
            return;
        }
        info(msg, args);
    }
}
