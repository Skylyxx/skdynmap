package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Line Color")
@Description("This expression will return the line-color property of the provided style. (Return a text)")
@Examples({"set {_linecolor} to line-color of {_style}",
        "set line-color of {_style} to \"##2a6c80\"",
        "reset {_style}'s line color"
})
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class ExprLineColor extends SimplePropertyExpression<AreaStyle, String> {

    static {
        register(ExprLineColor.class, String.class,
                "line(-| )color",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public String convert(AreaStyle style) {
        return style.getLineColor();
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
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (AreaStyle style : getExpr().getArray(e)) {
            if (mode == Changer.ChangeMode.SET) {
                style.setLineColor((String) delta[0]);
            } else if (mode == Changer.ChangeMode.RESET) {
                style.setLineColor(Util.getDefaultStyle().getLineColor());
            }
        }
    }
}
