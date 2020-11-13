package fr.skylyxx.skdynmap.utils;

import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;

public class AreaBuilder {

    private String name;
    private String description;
    private World world;
    private AreaStyle style;
    private Location pos1;
    private Location pos2;

    private String markerid;

    public AreaBuilder(String name, @Nullable String description, World world, AreaStyle style, Location pos1, Location pos2) {
        this.name = name;
        this.description = description;
        this.world = world;
        this.style = style;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public AreaBuilder() {
        this.name = null;
        this.description = null;
        this.world = null;
        this.style = null;
        this.pos1 = null;
        this.pos2 = null;
    }

    @Override
    public String toString() {
        return "builder of dynmap area named " + this.name + " in world " + this.world.getName();
    }

    // GET

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

    @Nullable
    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    @Nullable
    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    @Nullable
    public String getMarkerid() {
        return Util.formatMarkerID(name, world);
    }
}
