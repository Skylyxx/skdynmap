package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Fill Opacity")
@Description("This expression will return the fill-opacity property of the provided style. (Return a number)")
@Examples({"set {_fillopacity} to fill opacity of {_style}",
        "set fill opacity of {_style} to 0.4",
        "reset {_style}'s fill opacity"
})
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class ExprFillOpacity extends SimplePropertyExpression<AreaStyle, Number> {

    static {
        register(ExprFillOpacity.class, Number.class,
                "fill(-| )opacity",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public Number convert(AreaStyle style) {
        return style.getFillOpacity();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "fill opacity";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Number.class, Double.class, Integer.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (AreaStyle style : getExpr().getArray(e)) {
            if (mode == Changer.ChangeMode.SET) {
                style.setFillOpacity(((Number) delta[0]).doubleValue());
            } else if (mode == Changer.ChangeMode.RESET) {
                style.setFillOpacity(Util.getDefaultStyle().getFillOpacity());
            }
        }
    }
}
