package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Description of area")
@Description("Returns the description of an area/areabuilder\n" +
        "It can be get, set, reset or removed.")
@Since("1.0.1")
@Examples("set description of {_area} to \"Hello World\" !")
@RequiredPlugins("dynmap")
public class ExprDescription extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprDescription.class, String.class,
                "description",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public String convert(Object o) {
        if (o instanceof DynmapArea) {
            return ((DynmapArea) o).getDescription();
        } else if (o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getDescription();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "description";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if(mode != Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setDescription("");
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setDescription("");
                }
            }
            return;
        }
        for (Object o : getExpr().getArray(e)) {
            if (o instanceof DynmapArea) {
                ((DynmapArea) o).setDescription((String) delta[0]);
            } else if (o instanceof AreaBuilder) {
                ((AreaBuilder) o).setDescription((String) delta[0]);
            }
        }
    }
}
