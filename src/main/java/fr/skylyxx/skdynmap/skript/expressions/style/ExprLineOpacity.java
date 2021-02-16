package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Line opacity")
@Description("Returns the line-opacity property of the provided style.")
@Since("1.0-beta02")
@Examples("set {_lineopacity} to line opacity of {_style}")
@RequiredPlugins("dynmap")
public class ExprLineOpacity extends SimplePropertyExpression<AreaStyle, Double> {

    static {
        register(ExprLineOpacity.class, Double.class,
                "line(-| )opacity",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public Double convert(AreaStyle areaStyle) {
        return areaStyle.getLineOpacity();
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    protected String getPropertyName() {
        return "line opacity";
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
                    style.setLineOpacity(Config.DEFAULT_STYLE.getLineOpacity());
                }
                break;
            case SET:
                for (AreaStyle style : getExpr().getArray(e)) {
                    style.setLineOpacity(((Number) delta[0]).doubleValue());
                }
                break;
            default:
                return;
        }
    }

}
