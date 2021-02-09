package fr.skylyxx.skdynmap.utils.types;

import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.Arrays;

public class AreaBuilder {

    private String id;
    private String name;
    private String desc;
    private Location[] locations;
    private AreaStyle areaStyle;

    public AreaBuilder(String id, String name, String desc, Location[] locations, AreaStyle areaStyle) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.locations = locations;
        this.areaStyle = areaStyle;
    }

    public AreaBuilder() {
        this.id = null;
        this.name = null;
        this.desc = "";
        this.locations = null;
        this.areaStyle = Config.DEFAULT_STYLE.clone();
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
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String desc) {
        this.desc = desc;
    }
    public void setLocations(Location[] locations) {
        this.locations = locations;
    }
    public void setAreaStyle(AreaStyle areaStyle) {
        this.areaStyle = areaStyle;
    }

    public DynmapArea createArea() throws IOException, InvalidConfigurationException {
        if(getId() == null) {
            setId(getName().toLowerCase().replaceAll(" ", "-") + "_" + getLocations()[0].getWorld().getName().toLowerCase());
        }
        DynmapArea area = new DynmapArea(getId(), getName(), getDescription(), getLocations(), getAreaStyle());
        SkDynmap.getINSTANCE().getStorageYaml().setArea("areas." + getId(), area);
        SkDynmap.getINSTANCE().saveStorageYaml();
        SkDynmap.getINSTANCE().reloadStorageConfig();
        return area;
    }

    @Override
    public String toString() {
        return "AreaBuilder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", locations=" + Arrays.toString(locations) +
                ", areaStyle=" + areaStyle +
                '}';
    }
}
