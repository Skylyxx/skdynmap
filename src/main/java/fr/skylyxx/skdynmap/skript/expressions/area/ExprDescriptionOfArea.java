package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Description of area")
@Description({"This expression returns the description of an area or an area builder.\n",
        "You can use this expression to define the description of an area or an area builder."
})
@Since("1.0.1")
@Examples({
        "command /description:",
        "\ttrigger:",
        "\t\tset {_area} to area named \"My Area\" in world of player",
        "\t\tsend {_area}'s description to player"
})
@RequiredPlugins("dynmap")
public class ExprDescriptionOfArea extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprDescriptionOfArea.class, String.class,
                "[[sk]dynmap] description",
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
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setDescription((String) delta[0]);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setDescription((String) delta[0]);
                }
            }
        } else if (mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setDescription(null);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setDescription(null);
                }
            }
        }
    }
}
