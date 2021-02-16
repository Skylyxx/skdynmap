package fr.skylyxx.skdynmap.skript.expressions.markers.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.skript.scopes.ScopeMakeMarker;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprLastMarker extends SimpleExpression<MarkerBuilder> {

    static {
        Skript.registerExpression(ExprLastMarker.class, MarkerBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] marker"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected MarkerBuilder[] get(Event e) {
        return new MarkerBuilder[]{ScopeMakeMarker.lastMarker};
    }

    @Override
    public Class<? extends MarkerBuilder> getReturnType() {
        return MarkerBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last generated marker";
    }
}