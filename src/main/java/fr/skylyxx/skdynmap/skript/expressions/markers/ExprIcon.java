package fr.skylyxx.skdynmap.skript.expressions.markers;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Icon of marker")
@Description("Returns the icon of a marker/markerbuilder\n" +
        "It can be get, set or reset.\n" +
        "There is a list of all default dynmap icons: https://github.com/webbukkit/dynmap/wiki/Using-markers#marker-icons")
@Since("1.1")
@Examples("set icon of {_marker} to \"house\"")
@RequiredPlugins("dynmap")
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
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode != Changer.ChangeMode.SET) {
            for (Object o : getExpr().getArray(e)) {
                if (o instanceof DynmapMarker) {
                    ((DynmapMarker) o).setMarkerIcon(Config.DEFAULT_MARKER_ICON.get());
                } else if (o instanceof MarkerBuilder) {
                    ((MarkerBuilder) o).setMarkerIcon(Config.DEFAULT_MARKER_ICON.get());
                }
            }
        }
        for (Object o : getExpr().getArray(e)) {
            if (o instanceof DynmapMarker) {
                ((DynmapMarker) o).setMarkerIcon((String) delta[0]);
            } else if (o instanceof MarkerBuilder) {
                ((MarkerBuilder) o).setMarkerIcon((String) delta[0]);
            }
        }
    }
}
