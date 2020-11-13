package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Name of area")
@Description({"This expression returns the name of an area or an area builder.\n",
        "You can use this expression to define the name of an area or an area builder."
})
@Since("1.0.1")
@Examples({
        "command /getname:\n",
        "\ttrigger:\n",
        "\t\tset {_area} to area named \"My Area\" in world of player\n",
        "\t\tsend {_area}'s name to player"
})
@RequiredPlugins("dynmap")
public class ExprNameOfArea extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprNameOfArea.class, String.class,
                "[[sk]dynmap] name",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public String convert(Object o) {
        if (o instanceof DynmapArea) {
            return ((DynmapArea) o).getName();
        } else if (o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getName();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "name";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setName((String) delta[0]);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setName((String) delta[0]);
                }
            }
        }
    }
}
