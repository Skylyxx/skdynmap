package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Fill Color")
@Description("This expression will return the fill-color property of the provided style. (Return a text)")
@Examples({"set {_fillcolor} to fill color of {_style}",
        "set fill color of {_style} to \"##20b848\"",
        "reset {_style}'s fill color"
})
@Since("1.0-beta02")
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
    public String convert(AreaStyle style) {
        return style.getFillColor();
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
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (AreaStyle style : getExpr().getArray(e)) {
            if (mode == Changer.ChangeMode.SET) {
                style.setFillColor((String) delta[0]);
            } else if (mode == Changer.ChangeMode.RESET) {
                style.setFillColor(Util.getDefaultStyle().getFillColor());
            }
        }
    }
}
