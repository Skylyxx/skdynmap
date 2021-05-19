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
import org.jetbrains.annotations.Nullable;

@Name("Marker with id")
@Description("Get an marker from its id")
@Since("1.1")
@Examples("set {_marker} to marker with id \"my_marker_id\"")
@RequiredPlugins("dynmap")
public class ExprMarkerWithID extends SimpleExpression<DynmapMarker> {

    static {
        Skript.registerExpression(ExprMarkerWithID.class, DynmapMarker.class, ExpressionType.SIMPLE,
                "[dynmap] marker with id %string%"
        );
    }

    Expression<String> idExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idExpr = (Expression<String>) exprs[0];
        return true;
    }


    @Nullable
    @Override
    protected DynmapMarker[] get(Event e) {
        return new DynmapMarker[]{Util.getMarkerByID(idExpr.getSingle(e))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends DynmapMarker> getReturnType() {
        return DynmapMarker.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "dynmap marker with id " + idExpr.toString(e, debug);
    }
}
