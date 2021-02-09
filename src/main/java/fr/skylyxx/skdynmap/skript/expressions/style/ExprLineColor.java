package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprLineColor extends SimplePropertyExpression<AreaStyle, String> {

    static {
        register(ExprLineColor.class, String.class,
                "line(-| )color",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public String convert(AreaStyle areaStyle) {
        return areaStyle.getLineColor();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "line color";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if(mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        switch(mode) {
            case RESET:
                for(AreaStyle style : getExpr().getArray(e)) {
                    style.setLineColor(Config.DEFAULT_STYLE.getLineColor());
                }
                break;
            case SET:
                for(AreaStyle style : getExpr().getArray(e)) {
                    style.setLineColor((String)delta[0]);
                }
                break;
            default:
                return;
        }
    }

}
