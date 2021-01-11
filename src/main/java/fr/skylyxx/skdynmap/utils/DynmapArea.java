package fr.skylyxx.skdynmap.utils;

import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class DynmapArea {

    public final SkDynmap skdynmap = SkDynmap.getInstance();

    private String name;
    private World world;

    private String markerid;
    private String description;
    private AreaStyle style;
    private Location[] locations;

    public DynmapArea(String name, World world) {
        this.name = name;
        this.world = world;
        YamlConfiguration areas = skdynmap.getAreasConfig();

        String markerid = world.getName() + "_" + name.replaceAll(" ", "-");
        markerid = markerid.toLowerCase();

        if (areas.get("areas." + markerid) != null) {
            String description = areas.getString("areas." + markerid + ".description");

            ArrayList<Location> locationsArrayList = (ArrayList<Location>) skdynmap.getAreasConfig().get("areas." + markerid + ".location.locations");
            Location[] locations = new Location[locationsArrayList.size()];
            locations = locationsArrayList.toArray(locations);

            AreaStyle style = new AreaStyle(areas.getString("areas." + markerid + ".style.line.color"), areas.getDouble("areas." + markerid + ".style.line.opacity"), areas.getInt("areas." + markerid + ".style.line.weight"), areas.getString("areas." + markerid + ".style.fill.color"), areas.getDouble("areas." + markerid + ".style.fill.opacity"));
            if (description == null) {
                description = "";
            }
            this.description = description;
            this.locations = locations;
            this.style = style;
            this.markerid = (world.getName() + "_" + name.replaceAll(" ", "-")).toLowerCase();
        }
    }

    public DynmapArea() {
        this.name = null;
        this.world = null;
        this.description = null;
        this.locations = null;
        this.style = null;
        this.markerid = null;
    }

    @Nullable
    public String getMarkerid() {
        return markerid;
    }

    @Nullable
    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        skdynmap.getAreasConfig().set("areas." + markerid + ".name", name);
        setMarkerid(name, world);
    }

    @Nullable
    public final World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        skdynmap.getAreasConfig().set("areas." + markerid + ".location.world", world.getName());
        setMarkerid(name, world);
    }

    @Nullable
    public final String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
        skdynmap.getAreasConfig().set("areas." + markerid + ".description", description);
        Util.saveAreas();
    }

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
        skdynmap.getAreasConfig().set("areas." + markerid + ".location.locations", locations);
        Util.saveAreas();
    }

    @Nullable
    public final AreaStyle getStyle() {
        return style;
    }

    public void setStyle(AreaStyle style) {
        this.style = style;
        Util.setAreaStyle(this, style);
    }

    private void setMarkerid(String name, World world) {
        String oldMarkerid = this.markerid;
        this.markerid = (world.getName() + "_" + name.replaceAll(" ", "-")).toLowerCase();

        Util.runCopy(skdynmap.getAreasConfig(), "areas." + oldMarkerid, skdynmap.getAreasConfig(), "areas." + this.markerid);
        Util.deleteArea(oldMarkerid);
    }

    @Override
    public String toString() {
        return "dynmap area named " + name + " in world " + world;
    }
}

