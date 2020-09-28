package fr.skylyxx.skdynmap.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class ExprLineOpacity extends SimpleExpression<Double> {

    static {
        Skript.registerExpression(ExprLineOpacity.class, Double.class, ExpressionType.SIMPLE, "(line-opacity|line opacity) of %areastyle%");
    }

    private Expression<AreaStyle> style;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        style = (Expression<AreaStyle>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Double[] get(Event e) {
        if (style.getSingle(e) == null) {
            return null;
        }
        Double lineOpacity = style.getSingle(e).getLineOpacity();
        return new Double[]{lineOpacity};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "line-opacity of areastyle";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Double.class);
        } else {
            Util.log("Style properties can only be reset or set !", Level.SEVERE);
            return null;
        }
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            style.getSingle(e).setLineOpacity((Double) delta[0]);
        } else if (mode == Changer.ChangeMode.RESET) {
            style.getSingle(e).setLineOpacity(Util.getDefaultStyle().getLineOpacity());
        } else {
            Util.log("Style properties can only be reset or set !", Level.SEVERE);
        }
    }
}
