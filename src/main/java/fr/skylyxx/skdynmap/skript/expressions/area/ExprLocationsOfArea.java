package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import fr.skylyxx.skdynmap.utils.skript.MultiplyPropertyExpression;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Locations of area")
@Description({"This expression returns the locations of an area or an area builder.\n",
        "You can use this expression to define the locations of an area or an area builder."
})
@Since("1.0.1")
@Examples({
        "command /goto <text>:\n",
        "\ttrigger:\n",
        "\t\tset {_area} to area named arg in world of player\n",
        "\t\tset {_loc::*} to {_area}'s locations\n",
        "\t\tloop {_loc::*}:\n",
        "\t\t\tsend \"%loop-index% &6- &r%loop-value%\""
})
@RequiredPlugins("dynmap")
public class ExprLocationsOfArea extends MultiplyPropertyExpression<Object, Location> {

    static {
        register(ExprLocationsOfArea.class, Location.class,
                "[[sk]dynmap] location[s]",
                "dynmaparea/areabuilder"
        );
    }


    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    protected String getPropertyName() {
        return "locations";
    }

    @Nullable
    @Override
    public Location[] convert(Object o) {
        if (o instanceof DynmapArea) {
            try {
                Location pos1 = ((DynmapArea) o).getPos1();
                Location pos2 = ((DynmapArea) o).getPos2();
                return new Location[]{pos1, pos2};
            } catch (NullPointerException err) {
                return null;
            }
        } else if (o instanceof AreaBuilder) {
            try {
                Location pos1 = ((AreaBuilder) o).getPos1();
                Location pos2 = ((AreaBuilder) o).getPos2();
                return new Location[]{pos1, pos2};
            } catch (NullPointerException err) {
                return null;
            }
        }
        return null;

    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Location[].class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapArea) {
                    ((DynmapArea) o).setPos1((Location) delta[0]);
                    ((DynmapArea) o).setPos2((Location) delta[1]);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setPos1((Location) delta[0]);
                    ((AreaBuilder) o).setPos2((Location) delta[1]);
                }
            }
        }
    }
}