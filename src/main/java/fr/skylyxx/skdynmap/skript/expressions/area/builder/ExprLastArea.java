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

@Name("The area")
@Description("This expression returns the last generated area using area scope")
@Since("1.1")
@Examples("command /makearea:\n" +
        "\ttrigger:\n" +
        "\t\tmake area:\n" +
        "\t\t\tset name of area to \"My Area\"\n" +
        "\t\t\tset description of area to \"Using sections !\"\n" +
        "\t\t\tset locations of area to {pos-1} and {pos-2}\n" +
        "\t\t\tset {_style} to default area style\n" +
        "\t\t\tset line color of {_style} to \"##00FF00\"\n" +
        "\t\t\tset fill color of {_style} to \"##FFFF00\"\n" +
        "\t\t\tset style of area to {_style}\n" +
        "\t\tcreate area from last generated area\n" +
        "\t\tsend \"Area created !\"")
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