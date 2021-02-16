package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Fill color")
@Description("Returns the fill-color property of the provided style.")
@Since("1.0-beta02")
@Examples("set {_fillcolor} to fill color of {_style}")
@RequiredPlugins("dynmap")
public class ExprFillColor extends SimplePropertyExpression<AreaStyle, String> {

    static {
        register(ExprFillColor.class, String.class,
                "fill(-| )color",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public String convert(AreaStyle areaStyle) {
        return areaStyle.getFillColor();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "fill color";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        switch (mode) {
            case RESET:
                for (AreaStyle style : getExpr().getArray(e)) {
                    style.setFillColor(Config.DEFAULT_STYLE.getFillColor());
                }
                break;
            case SET:
                for (AreaStyle style : getExpr().getArray(e)) {
                    style.setFillColor((String) delta[0]);
                }
                break;
            default:
                return;
        }
    }
}
