package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;



public class EffCreateArea extends Effect {

    static {
        Skript.registerEffect(EffCreateArea.class,
            "create [new] [dynmap] area named %string% in %world% between %location% and %location%",
            "create [new] [dynmap] area named %string% with description %string% in %world% between %location% and %location%",
            "create [new] [dynmap] area named %string% in %world% between %location% and %location% with style %strings%",
            "create [new] [dynmap] area named %string% with description %string% in %world% between %location% and %location% with style %strings%"
        );
    }

    private Expression<String> name;
    private Expression<String> description;
    private Expression<World> world;
    private Expression<Location> pos1;
    private Expression<Location> pos2;
    private Expression<String> styleString;

    private AreaStyle style;
    private SkDynmap skdynmap;

    private String lineColor;
    private double lineOpacity;
    private int lineWeight;
    private String fillColor;
    private double fillOpacity;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, SkriptParser.ParseResult paramParseResult) {
        name = (Expression<String>) expr[0];
        if (matchedPattern == 0) { //without desc / without style
            world = (Expression<World>) expr[1];
            pos1 = (Expression<Location>) expr[2];
            pos2 = (Expression<Location>) expr[3];

        } else if (matchedPattern == 1) { //with desc / without style
            description = (Expression<String>) expr[1];
            world = (Expression<World>) expr[2];
            pos1 = (Expression<Location>) expr[3];
            pos2 = (Expression<Location>) expr[4];

        } else if (matchedPattern == 2) { //without desc / with style
            world = (Expression<World>) expr[1];
            pos1 = (Expression<Location>) expr[2];
            pos2 = (Expression<Location>) expr[3];
            styleString = (Expression<String>) expr[4];

        } else if (matchedPattern == 3) { // with desc / with style
            description = (Expression<String>) expr[1];
            world = (Expression<World>) expr[2];
            pos1 = (Expression<Location>) expr[3];
            pos2 = (Expression<Location>) expr[4];
            styleString = (Expression<String>) expr[5];

        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (styleString == null) {
            style = Util.getDefaultStyle();
        } else {
            String[] styleStringArray = styleString.getArray(e);

            if (!styleStringArray[0].equalsIgnoreCase("default")) { // FILL-COLOR
                fillColor = styleStringArray[0];
            } else {
                fillColor = skdynmap.getConfig().getString("default-style.fill.color");
            }

            if (!styleStringArray[1].equalsIgnoreCase("default")) { // FILL-OPACITY
                fillOpacity = Double.parseDouble(styleStringArray[1]);
            } else {
                fillOpacity = skdynmap.getConfig().getDouble("default-style.fill.opacity");
            }

            if (!styleStringArray[2].equalsIgnoreCase("default")) { // LINE-COLOR
                lineColor = styleStringArray[2];
            } else {
                lineColor = skdynmap.getConfig().getString("default-style.line.color");
            }

            if (!styleStringArray[3].equalsIgnoreCase("default")) { // LINE-OPACITY
                lineOpacity = Double.parseDouble(styleStringArray[3]);
            } else {
                lineOpacity = skdynmap.getConfig().getDouble("default-style.line.opacity");
            }

            if (!styleStringArray[4].equalsIgnoreCase("default")) { // LINE-WEIGHT
                lineWeight = Integer.parseInt(styleStringArray[4]);
            } else {
                lineWeight = skdynmap.getConfig().getInt("default-style.line.weight");
            }

            style = new AreaStyle(lineColor, lineOpacity, lineWeight, fillColor, fillOpacity);
        }
        if (description == null) {
            Util.createArea(world.getSingle(e), name.getSingle(e), null, pos1.getSingle(e), pos2.getSingle(e), style);
        } else {
            Util.createArea(world.getSingle(e), name.getSingle(e), description.getSingle(e), pos1.getSingle(e), pos2.getSingle(e), style);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        if (description == null && styleString == null) { //without desc / without style
            return "create new dynmap area named " + name.toString(e, debug) + " in world " + world.toString(e, debug) + " between " + pos1.toString(e, debug).toString() + " and " + pos2.toString(e, debug).toString();
        } else if (description != null && styleString == null) { //with desc / without style
            return "create new dynmap area named " + name.toString(e, debug) + " with description " + description.toString(e, debug) + " in world " + world.toString(e, debug) + " between " + pos1.toString(e, debug).toString() + " and " + pos2.toString(e, debug).toString();
        } else if (description == null && styleString != null) { //without desc / with style
            return "create new dynmap area named " + name.toString(e, debug) + " in world " + world.toString(e, debug) + " between " + pos1.toString(e, debug).toString() + " and " + pos2.toString(e, debug).toString() + " with style " + styleString.toString(e, debug).toString();
        } else if (description != null && styleString != null) { // with desc / with style
            return "create new dynmap area named " + name.toString(e, debug) + " with description " + description.toString(e, debug) + " in world " + world.toString(e, debug) + " between " + pos1.toString(e, debug).toString() + " and " + pos2.toString(e, debug).toString() + " with style " + styleString.toString(e, debug).toString();
        }
        return null;
    }
}
