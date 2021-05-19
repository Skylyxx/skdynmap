package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Default style")
@Description("Returns the default style defined in the config.")
@Since("1.0-beta02")
@Examples("set {_style} to default style")
@RequiredPlugins("dynmap")
public class ExprDefaultStyle extends SimpleExpression<AreaStyle> {

    static {
        Skript.registerExpression(ExprDefaultStyle.class, AreaStyle.class, ExpressionType.SIMPLE,
                "default [area] style"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected AreaStyle[] get(Event e) {
        return new AreaStyle[]{Config.DEFAULT_STYLE.clone()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AreaStyle> getReturnType() {
        return AreaStyle.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "default area style";
    }


}
