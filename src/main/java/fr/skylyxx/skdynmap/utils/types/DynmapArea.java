package fr.skylyxx.skdynmap.utils.types;

import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;

import java.util.Arrays;

public class DynmapArea {

    private static final SkDynmap skDynmap = SkDynmap.getINSTANCE();
    private String id;
    private String name;
    private String desc;
    private Location[] locations;
    private AreaStyle areaStyle;

    public DynmapArea(String id, String name, String desc, Location[] locations, AreaStyle areaStyle) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.locations = locations;
        this.areaStyle = areaStyle;

        skDynmap.dynmapAreas.put(id, this);
    }

    public DynmapArea(AreaBuilder areaBuilder) {
        this.id = areaBuilder.getId();
        this.name = areaBuilder.getName();
        this.desc = areaBuilder.getDescription();
        this.locations = areaBuilder.getLocations();
        this.areaStyle = areaBuilder.getAreaStyle();

        skDynmap.dynmapAreas.put(id, this);
    }

    public DynmapArea(String id) {
        DynmapArea area = skDynmap.getStorageYaml().getArea("areas." + id);
        if (area == null) {
            return;
        }
        this.id = area.getId();
        this.name = area.getName();
        this.desc = area.getDescription();
        this.locations = area.getLocations();
        this.areaStyle = area.getAreaStyle();
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

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
    }

    public AreaStyle getAreaStyle() {
        return areaStyle;
    }

    public void setAreaStyle(AreaStyle areaStyle) {
        this.areaStyle = areaStyle;
    }

    @Override
    public String toString() {
        return "DynmapArea{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", locations=" + Arrays.toString(locations) +
                ", areaStyle=" + areaStyle +
                '}';
    }

    public void deleteArea() {
        skDynmap.dynmapAreas.remove(getId());
    }
}
