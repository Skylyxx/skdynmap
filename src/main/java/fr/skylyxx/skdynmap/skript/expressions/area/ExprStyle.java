package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprStyle extends SimplePropertyExpression<Object, AreaStyle> {

    static {
        register(ExprStyle.class, AreaStyle.class,
                "[area]style",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public AreaStyle convert(Object o) {
        if (o instanceof DynmapArea) {
            return ((DynmapArea) o).getAreaStyle();
        } else if (o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getAreaStyle();
        }
        return null;
    }

    @Override
    public Class<? extends AreaStyle> getReturnType() {
        return AreaStyle.class;
    }

    @Override
    protected String getPropertyName() {
        return "area style";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(AreaStyle.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (Object o : getExpr().getArray(e)) {
            if (o instanceof DynmapArea) {
                ((DynmapArea) o).setAreaStyle((AreaStyle) delta[0]);
            } else if (o instanceof AreaBuilder) {
                ((AreaBuilder) o).setAreaStyle((AreaStyle) delta[0]);
            }
        }
    }
}
