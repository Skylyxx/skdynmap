package fr.skylyxx.skdynmap.skript.expressions.area.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.skript.scopes.ScopeMakeArea;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.skript.EffectSection;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - The area")
@Description("If it's inside of an area scope, this expression returns the area that belong to that scope")
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
public class ExprArea extends SimpleExpression<AreaBuilder> {

    static {
        Skript.registerExpression(ExprArea.class, AreaBuilder.class, ExpressionType.SIMPLE,
                "[the] area"
        );
    }

    private boolean scope = false;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        scope = EffectSection.isCurrentSection(ScopeMakeArea.class);
        if(!scope) {
            Skript.error("You can't use '" + parseResult.expr + "' outside of a Make Area scope !");
        }
        return true;
    }

    @Nullable
    @Override
    protected AreaBuilder[] get(Event e) {
        if (scope) {
            return new AreaBuilder[]{ScopeMakeArea.lastArea};
        }
        return null;
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
        return "the area";
    }
}

