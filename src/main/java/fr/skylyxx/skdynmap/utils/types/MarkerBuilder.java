package fr.skylyxx.skdynmap.utils.types;

import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.dynmap.markers.MarkerIcon;

import javax.annotation.Nullable;
import java.io.IOException;

public class MarkerBuilder {

    private String id;
    private String name;
    private Location location;
    private MarkerIcon markerIcon;

    public MarkerBuilder(@Nullable String id, String name, Location location, String iconID) {
        this.id = id;
        this.name = name;
        this.location = location;
        setMarkerIcon(iconID);
    }

    public MarkerBuilder() {
        this.id = null;
        this.name = null;
        this.location = null;
        setMarkerIcon(Config.DEFAULT_MARKER_ICON);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public MarkerIcon getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(String iconID) {
        this.markerIcon = SkDynmap.getINSTANCE().getMarkerAPI().getMarkerIcon(iconID);
    }

    public DynmapMarker createMarker() throws IOException, InvalidConfigurationException {
        if (getId() == null) {
            setId(getName().toLowerCase().replaceAll(" ", "-") + "_" + location.getWorld().getName().toLowerCase());
        }
        DynmapMarker dynmapMarker = new DynmapMarker(getId(), getName(), getLocation(), getMarkerIcon().getMarkerIconID());
        return dynmapMarker;
    }

    @Override
    public String toString() {
        return "MarkerBuilder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", markerIcon=" + markerIcon.getMarkerIconID() +
                '}';
    }
}
