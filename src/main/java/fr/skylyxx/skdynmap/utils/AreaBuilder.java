package fr.skylyxx.skdynmap.utils;

import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;

public class AreaBuilder {

    private String name;
    private String description;
    private World world;
    private AreaStyle style;
    private Location[] locations;

    private String markerid;

    public AreaBuilder(String name, @Nullable String description, World world, AreaStyle style, Location[] locations) {
        this.name = name;
        this.description = description;
        this.world = world;
        this.style = style;
        this.locations = locations;
    }

    public AreaBuilder() {
        this.name = null;
        this.description = null;
        this.world = null;
        this.style = null;
        this.locations = null;
    }

    @Override
    public String toString() {
        return "builder of dynmap area named " + this.name + " in world " + this.world.getName();
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Nullable
    public AreaStyle getStyle() {
        return style;
    }

    // SET

    public void setStyle(AreaStyle style) {
        this.style = style;
    }

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
    }

    @Nullable
    public String getMarkerid() {
        return Util.formatMarkerID(name, world);
    }

    public void setMarkerid(String markerid) {
        this.markerid = markerid;
    }
}
