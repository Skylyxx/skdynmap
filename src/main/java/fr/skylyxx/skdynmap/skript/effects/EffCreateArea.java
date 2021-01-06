package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;


@Name("SkDynmap - Create an area")
@Description("This effect allow you to create an area on the map")
@Examples({
        "command /createarea:\n",
        "\ttrigger:\n",
        "\t\tset {_area} to new area named \"My Area\" in world of player between {pos1} and {pos2}\n",
        "\t\tcreate {_area}"
})
@Since("1.0-beta02 - Syntax changed in 1.0.1")
@RequiredPlugins("dynmap")
public class EffCreateArea extends Effect {

    static {
        Skript.registerEffect(EffCreateArea.class,
                "create %areabuilder%"
        );
    }


    private Expression<AreaBuilder> areaBuilder;

    private SkDynmap skdynmap;

    private String lineColor;
    private double lineOpacity;
    private int lineWeight;
    private String fillColor;
    private double fillOpacity;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, SkriptParser.ParseResult paramParseResult) {
        skdynmap = SkDynmap.getInstance();
        areaBuilder = (Expression<AreaBuilder>) expr[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        AreaBuilder builder = areaBuilder.getSingle(e);
        if (builder.getName() == null ||
                builder.getWorld() == null ||
                builder.getStyle() == null ||
                builder.getPos1() == null ||
                builder.getPos2() == null) {
            Skript.error("Cannot create an area, one (or more) of its values is null");
            return;
        }
        Util.createArea(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return areaBuilder.toString(e, debug);
    }
}
