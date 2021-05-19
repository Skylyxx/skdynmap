package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.MultiplyPropertyExpression;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Locations of area")
@Description("Returns the locations of an area/areabuilder\n" +
        "It can be get, and set.")
@Since("1.0.1")
@Examples("set locations of {_area} to {pos-1} and {pos-2}")
@RequiredPlugins("dynmap")
public class ExprLocations extends MultiplyPropertyExpression<Object, Location> {

    static {
        register(ExprLocations.class, Location.class,
                "locations",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public Location[] convert(Object o) {
        if (o instanceof DynmapArea) {
            return ((DynmapArea) o).getLocations();
        } else if (o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getLocations();
        }
        return null;
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
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Location[].class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (Object o : getExpr().getArray(e)) {
            if (o instanceof DynmapArea) {
                ((DynmapArea) o).setLocations((Location[]) delta);
            } else if (o instanceof AreaBuilder) {
                ((AreaBuilder) o).setLocations((Location[]) delta);
            }
        }
    }
}
