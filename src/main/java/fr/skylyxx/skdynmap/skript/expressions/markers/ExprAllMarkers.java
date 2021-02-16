package fr.skylyxx.skdynmap.skript.expressions.markers;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("All markers")
@Description("Return a list with all created markers")
@Since("1.1")
@Examples("command /deleteall:\n" +
        "\ttrigger:\n" +
        "\t\tloop all markers:\n" +
        "\t\t\tdelete marker loop-value")
@RequiredPlugins("dynmap")
public class ExprAllMarkers extends SimpleExpression<DynmapMarker> {

    static {
        Skript.registerExpression(ExprAllMarkers.class, DynmapMarker.class, ExpressionType.SIMPLE,
                "all [dynmap] markers"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends DynmapMarker> getReturnType() {
        return DynmapMarker.class;
    }

    @Nullable
    @Override
    protected DynmapMarker[] get(Event e) {
        return Util.getAllMarkers();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all dynmap markers";
    }
}
