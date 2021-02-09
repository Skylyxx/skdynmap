package fr.skylyxx.skdynmap.utils.types;

import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class DynmapArea {

    private String id;
    private String name;
    private String desc;
    private Location[] locations;

    private SkDynmap skDynmap = SkDynmap.getINSTANCE();

    public DynmapArea(String id, String name, String desc, Location[] locations, AreaStyle areaStyle) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.locations = locations;
        this.areaStyle = areaStyle;
    }

    private AreaStyle areaStyle;

    public DynmapArea(AreaBuilder areaBuilder) {
        this.id = areaBuilder.getId();
        this.name = areaBuilder.getName();
        this.desc = areaBuilder.getDescription();
        this.locations = areaBuilder.getLocations();
        this.areaStyle = areaBuilder.getAreaStyle();
    }

    public DynmapArea(String id) {
        DynmapArea area = SkDynmap.getINSTANCE().getStorageYaml().getArea("areas." + id);
        if(area == null) {
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
    public String getName() {
        return name;
    }
    public String getDescription() {
        return desc;
    }
    public Location[] getLocations() {
        return locations;
    }
    public AreaStyle getAreaStyle() {
        return areaStyle;
    }

    public void setId(String id) {
        String oldId = this.id;
        this.id = id;
        try {
            skDynmap.getStorageYaml().rename("areas." + oldId, "areas." + id);
            skDynmap.saveStorageYaml();
        } catch (ExecutionException | InterruptedException | IOException e ) {
            e.printStackTrace();
        }

    }
    public void setName(String name) {
        this.name = name;
        skDynmap.getStorageYaml().set("areas." + id + ".name", name);
        try {
            skDynmap.saveStorageYaml();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setDescription(String desc) {
        this.desc = desc;
        skDynmap.getStorageYaml().set("areas." + id + ".description", desc);
        try {
            skDynmap.saveStorageYaml();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setLocations(Location[] locations) {
        this.locations = locations;
        skDynmap.getStorageYaml().set("areas." + id + ".locations", locations);
        try {
            skDynmap.saveStorageYaml();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAreaStyle(AreaStyle areaStyle) {
        this.areaStyle = areaStyle;
        skDynmap.getStorageYaml().setStyle("areas." + id + ".style", areaStyle);
        try {
            skDynmap.saveStorageYaml();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        skDynmap.getStorageYaml().set("areas." + getId(), null);
        try {
            skDynmap.saveStorageYaml();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
