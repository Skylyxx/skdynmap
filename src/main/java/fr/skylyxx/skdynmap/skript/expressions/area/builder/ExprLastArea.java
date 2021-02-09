package fr.skylyxx.skdynmap.skript.expressions.area.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.skript.scopes.ScopeMakeArea;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - The last generated area")
@Description("This expression returns the last generated area using area scope")
@Since("1.0.1")
@Examples({
        "command /createarea:",
        "\ttrigger:",
        "\t\tmake area:",
        "\t\t\tset area's name to \"My Area\"",
        "\t\t\tset area's description to \"My Description\"",
        "\t\t\tset area's world to world of player",
        "\t\t\tset area's style to default area style",
        "\t\t\tset area's location to {pos1} and {pos2}",
        "\t\tcreate the last area"
})
@RequiredPlugins("dynmap")
public class ExprLastArea extends SimpleExpression<AreaBuilder> {

    static {
        Skript.registerExpression(ExprLastArea.class, AreaBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] area"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected AreaBuilder[] get(Event e) {
        return new AreaBuilder[]{ScopeMakeArea.lastArea};
    }

    @Override
    public Class<? extends AreaBuilder> getReturnType() {
        return AreaBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last generated area";
    }
}