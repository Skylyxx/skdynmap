package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import org.bukkit.World;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Name of area")
@Description({"This expression returns the world of an area or an area builder.\n",
        "You can use this expression to define the world of an area or an area builder."
})
@Since("1.0.1")
@Examples({
        "command /getname:\n",
        "\ttrigger:\n",
        "\t\tset {_area} to area named \"My Area\" in world of player\n",
        "\t\tsend \"%{_area}'s world%\" to player"
})
@RequiredPlugins("dynmap")
public class ExprWorldOfArea extends SimplePropertyExpression<Object, World> {

    static {
        register(ExprWorldOfArea.class, World.class,
                "[[sk]dynmap] world",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public World convert(Object o) {
        if (o instanceof DynmapArea) {
            return ((DynmapArea) o).getWorld();
        } else if (o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getWorld();
        }
        return null;
    }

    @Override
    public Class<? extends World> getReturnType() {
        return World.class;
    }

    @Override
    protected String getPropertyName() {
        return "world";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(World.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setWorld((World) delta[0]);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setWorld((World) delta[0]);
                }
            }
        }
    }
}
