package fr.skylyxx.skdynmap.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

public class ExprDefaultStyle extends SimpleExpression {

    static {
        Skript.registerExpression(ExprDefaultStyle.class, AreaStyle.class, ExpressionType.SIMPLE, "(default|new) [dynmap] area style");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected AreaStyle[] get(Event e) {
        return new AreaStyle[]{Util.getDefaultStyle()};
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
    public String toString(Event e, boolean debug) {
        return Util.getDefaultStyle().toString();
    }
}
