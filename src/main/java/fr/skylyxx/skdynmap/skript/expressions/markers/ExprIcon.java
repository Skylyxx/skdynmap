package fr.skylyxx.skdynmap.skript.expressions.markers;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprIcon extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprIcon.class, String.class,
                "icon",
                "dynmapmarker/markerbuilder"
        );
    }

    @Nullable
    @Override
    public String convert(Object o) {
        if (o instanceof DynmapMarker) {
            return ((DynmapMarker) o).getMarkerIcon().getMarkerIconID();
        } else if (o instanceof MarkerBuilder) {
            return ((MarkerBuilder) o).getMarkerIcon().getMarkerIconID();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "icon";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (Object o : getExpr().getArray(e)) {
            if (o instanceof DynmapMarker) {
                ((DynmapMarker) o).setMarkerIcon((String) delta[0]);
            } else if (o instanceof MarkerBuilder) {
                ((MarkerBuilder) o).setMarkerIcon((String) delta[0]);
            }
        }
    }
}
