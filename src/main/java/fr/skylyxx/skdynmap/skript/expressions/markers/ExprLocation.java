package fr.skylyxx.skdynmap.skript.expressions.markers;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprLocation extends SimplePropertyExpression<Object, Location> {

    static {
        register(ExprLocation.class, Location.class,
                "location",
                "dynmapmarker/markerbuilder"
        );
    }

    @Nullable
    @Override
    public Location convert(Object o) {
        if (o instanceof DynmapMarker) {
            return ((DynmapMarker) o).getLocation();
        } else if (o instanceof MarkerBuilder) {
            return ((MarkerBuilder) o).getLocation();
        }
        return null;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    protected String getPropertyName() {
        return "location";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Location.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (Object o : getExpr().getArray(e)) {
            if (o instanceof DynmapMarker) {
                ((DynmapMarker) o).setLocation((Location) delta[0]);
            } else if (o instanceof MarkerBuilder) {
                ((MarkerBuilder) o).setLocation((Location) delta[0]);
            }
        }
    }
}
