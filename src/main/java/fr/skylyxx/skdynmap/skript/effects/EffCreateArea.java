package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.AreaMaker;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;


@Name("SkDynmap - Create an area")
@Description("This effect allow you to create an area on the map")
@Examples("create new area named \"My Area\" in world of player between {pos1} and {pos2}")
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class EffCreateArea extends Effect {

    static {
        Skript.registerEffect(EffCreateArea.class,
                "create %areamaker%"
        );
    }


    private Expression<AreaMaker> areaMaker;

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
        areaMaker = (Expression<AreaMaker>) expr[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Util.createArea(areaMaker.getSingle(e));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return areaMaker.toString(e, debug);
    }
}
