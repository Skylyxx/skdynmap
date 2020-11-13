package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Line Weight")
@Description("This expression will return the line weight property of the provided style. (Return an integer)")
@Examples({"set {_lineweight} to line-weight of {_style}",
        "set line weight of {_style} to 4",
        "reset {_style}'s line weight"
})
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class ExprLineWeight extends SimplePropertyExpression<AreaStyle, Number> {

    static {
        register(ExprLineWeight.class, Number.class,
                "line(-| )weight",
                "areastyle"
        );
    }

    @Nullable
    @Override
    public Number convert(AreaStyle style) {
        return style.getLineWeight();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "line weight";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Double.class, Integer.class, Number.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (AreaStyle style : getExpr().getArray(e)) {
            if (mode == Changer.ChangeMode.SET) {
                style.setLineWeight(((Number)delta[0]).intValue());
            } else if (mode == Changer.ChangeMode.RESET) {
                style.setLineWeight(Util.getDefaultStyle().getLineWeight());
            }
        }
    }
}
