package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Style of area")
@Description({"This expression returns the style of an area or an area builder.\n",
        "You can use this expression to define the style of an area or an area builder."
})
@Since("1.0.1")
@Examples({
        "command /getname:\n",
        "\ttrigger:\n",
        "\t\tset {_area} to area named \"My Area\" in world of player\n",
        "\t\tset {_style} to {_area}'s style\n",
        "\t\tset fill-color of {_style} to \"##20b848\"\n",
        "\t\tset style of {_area} to {_style}"
})
@RequiredPlugins("dynmap")
public class ExprStyleOfArea extends SimplePropertyExpression<Object, AreaStyle> {

    static {
        register(ExprStyleOfArea.class, AreaStyle.class,
                "[[sk]dynmap] style",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public AreaStyle convert(Object o) {
        if (o instanceof DynmapArea) {
            return ((DynmapArea) o).getStyle();
        } else if (o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getStyle();
        }
        return null;
    }

    @Override
    public Class<? extends AreaStyle> getReturnType() {
        return AreaStyle.class;
    }

    @Override
    protected String getPropertyName() {
        return "style";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(AreaStyle.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setStyle((AreaStyle) delta[0]);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setStyle((AreaStyle) delta[0]);
                }
            }
        } else if (mode == Changer.ChangeMode.RESET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setStyle(Util.getDefaultStyle());
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setStyle(Util.getDefaultStyle());
                }
            }
        }
    }
}
