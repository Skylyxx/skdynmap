package fr.skylyxx.skdynmap.skript.expressions.markers.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.skript.scopes.ScopeMakeMarker;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("The marker")
@Description("This expression returns the last generated marker using marker scope")
@Since("1.1")
@Examples("command /makemarker:\n" +
        "\ttrigger:\n" +
        "\t\tmake marker:\n" +
        "\t\t\tset name of marker to \"Landmark\"\n" +
        "\t\t\tset location of marker to {pos-2}\n" +
        "\t\t\tset marker's icon to \"house\"\n" +
        "\t\tcreate marker from last generated marker")
@RequiredPlugins("dynmap")
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