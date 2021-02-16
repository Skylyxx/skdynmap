package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Line weight")
@Description("Returns the line-weight property of the provided style.")
@Since("1.0-beta02")
@Examples("set {_lineweight} to line weight of {_style}")
@RequiredPlugins("dynmap")
public class ExprLineWeight extends SimplePropertyExpression<AreaStyle, Integer> {

    static {
        register(ExprLineWeight.class, Integer.class,
                "line(-| )weight",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public Integer convert(AreaStyle areaStyle) {
        return areaStyle.getLineWeight();
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    protected String getPropertyName() {
        return "line weight";
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
                    style.setLineWeight(Config.DEFAULT_STYLE.getLineWeight());
                }
                break;
            case SET:
                for (AreaStyle style : getExpr().getArray(e)) {
                    style.setLineWeight(((Number) delta[0]).intValue());
                }
                break;
            default:
                return;
        }
    }

}
