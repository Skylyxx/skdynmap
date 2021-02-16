package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprFillOpacity extends SimplePropertyExpression<AreaStyle, Double> {

    static {
        register(ExprFillOpacity.class, Double.class,
                "fill(-| )opacity",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public Double convert(AreaStyle areaStyle) {
        return areaStyle.getFillOpacity();
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    protected String getPropertyName() {
        return "fill opacity";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Double.class, Number.class, Integer.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        switch (mode) {
            case RESET:
                for (AreaStyle style : getExpr().getArray(e)) {
                    style.setFillOpacity(Config.DEFAULT_STYLE.getFillOpacity());
                }
                break;
            case SET:
                for (AreaStyle style : getExpr().getArray(e)) {
                    style.setFillOpacity(((Number) delta[0]).doubleValue());
                }
                break;
            default:
                return;
        }
    }

}
