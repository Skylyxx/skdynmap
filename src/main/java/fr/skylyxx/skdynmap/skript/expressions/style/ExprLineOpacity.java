package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Line Opacity")
@Description("This expression will return the line-opacity property of the provided style. (Return a numner)")
@Examples({"set {_lineopacity} to line-opacity of {_style}",
        "set line-opacity of {_style} to 0.8",
        "reset {_style}'s line opacity"
})
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class ExprLineOpacity extends SimplePropertyExpression<AreaStyle, Double> {

    static {
        register(ExprLineOpacity.class, Double.class,
                "line(-| )opacity",
                "areastyle");
    }

    @Nullable
    @Override
    public Double convert(AreaStyle style) {
        return style.getLineOpacity();
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
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Double.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (AreaStyle style : getExpr().getArray(e)) {
            if (mode == Changer.ChangeMode.SET) {
                style.setLineOpacity((Double) delta[0]);
            } else if (mode == Changer.ChangeMode.RESET) {
                style.setLineOpacity(Util.getDefaultStyle().getLineOpacity());
            }
        }
    }
}
