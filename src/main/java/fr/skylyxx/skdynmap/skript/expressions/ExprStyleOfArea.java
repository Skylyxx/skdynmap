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
import fr.skylyxx.skdynmap.utils.DynmapArea;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class ExprStyleOfArea extends SimpleExpression<AreaStyle> {

    static {
        Skript.registerExpression(ExprStyleOfArea.class, AreaStyle.class, ExpressionType.SIMPLE, "style of %dynmaparea%");
    }

    private Expression<DynmapArea> area;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        area = (Expression<DynmapArea>) exprs[0];
        return true;
    }


    @Nullable
    @Override
    protected AreaStyle[] get(Event e) {
        if (!Util.areaExist(area.getSingle(e))) {
            Util.log("Trying to get the style of a non-existent area !", Level.SEVERE);
            return null;
        }
        AreaStyle style = area.getSingle(e).getStyle();
        return new AreaStyle[]{style};
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
        return "style of area " + area.toString(e, debug);
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET) {
            if (!Util.areaExist(area.getSingle(e))) {
                Util.log("Trying to change the style of a non-existent area !", Level.SEVERE);
                return;
            }
            Util.setAreaStyle(area.getSingle(e), Util.getDefaultStyle());
        } else if (mode == Changer.ChangeMode.SET) {
            AreaStyle style = (AreaStyle) delta[0];
            if (style != null) {
                Util.setAreaStyle(area.getSingle(e), style);
            }
        } else {
            Util.log("The style of the zones can only be reset or set !", Level.SEVERE);
            return;
        }
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(AreaStyle.class);
        } else {
            return null;
        }
    }
}
