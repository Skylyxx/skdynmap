package fr.skylyxx.skdynmap.utils.types;

import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;
import org.dynmap.markers.MarkerIcon;

public class DynmapMarker {

    private static final SkDynmap skDynmap = SkDynmap.getINSTANCE();

    private String id;
    private String name;
    private Location location;
    private MarkerIcon markerIcon;

    public DynmapMarker(String id, String name, Location location, String iconID) {
        this.id = id;
        this.name = name;
        this.location = location;
        setMarkerIcon(iconID);

        skDynmap.dynmapMarkers.put(id, this);
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
        this.markerIcon = skDynmap.getMarkerAPI().getMarkerIcon(iconID);
    }

    @Override
    public String toString() {
        return "DynmapMarker{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", markerIcon=" + (markerIcon == null ? "null" : markerIcon.getMarkerIconID()) +
                '}';
    }

    public void deleteMarker() {
        skDynmap.dynmapMarkers.remove(getId());
    }
}
