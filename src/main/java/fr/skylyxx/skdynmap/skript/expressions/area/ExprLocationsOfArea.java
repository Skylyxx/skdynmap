package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.skript.MultiplyPropertyExpression;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.dynmap.markers.AreaMarker;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.logging.Level;

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
                Location[] locations = ((DynmapArea) o).getLocations();
                return locations;
            } catch (NullPointerException err) {
                return null;
            }
        } else if (o instanceof AreaBuilder) {
            try {
                Location[] locations = ((AreaBuilder) o).getLocations();
                return locations;
            } catch (NullPointerException err) {
                return null;
            }
        }
        return null;

    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if(mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Location[].class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if(mode == Changer.ChangeMode.SET) {
            for(Object o : getExpr().getArray(e)) {
                ArrayList<Location> locationsArrayList = new ArrayList<Location>();
                for(Location location : (Location[]) delta) {
                    locationsArrayList.add(location);
                }
                Location[] locations = new Location[locationsArrayList.size()];
                locations = locationsArrayList.toArray(locations);
                if(o instanceof DynmapArea) {
                    ((DynmapArea) o).setLocations(locations);
                } else if (o instanceof AreaBuilder) {
                    ((AreaBuilder) o).setLocations(locations);
                }
                Util.log("Finished", Level.WARNING);
            }
        }
    }
}