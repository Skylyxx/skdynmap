package fr.skylyxx.skdynmap.utils;

import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class DynmapArea {

    public final SkDynmap skdynmap = SkDynmap.getInstance();

    private final String name;
    private final World world;

    private String markerid;
    private String description;
    private AreaStyle style;
    private Location pos1;
    private Location pos2;

    public DynmapArea(final String name, final World world) {
        this.name = name;
        this.world = world;
        YamlConfiguration areas = skdynmap.getAreasConfig();

        String markerid = world.getName() + "_" + name.replaceAll(" ", "-");
        markerid = markerid.toLowerCase();

        if (areas.get("areas." + markerid) != null) {
            String description = areas.getString("areas." + markerid + ".description");
            Location pos1 = areas.getLocation("areas." + markerid + ".location.pos1");
            Location pos2 = areas.getLocation("areas." + markerid + ".location.pos2");
            AreaStyle style = new AreaStyle(areas.getString("areas." + markerid + ".style.line.color"), areas.getDouble("areas." + markerid + ".style.line.opacity"), areas.getInt("areas." + markerid + ".style.line.weight"), areas.getString("areas." + markerid + ".style.fill.color"), areas.getDouble("areas." + markerid + ".style.fill.opacity"));
            if (description == null) {
                description = "";
            }
            this.description = description;
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.style = style;
            this.markerid = (world.getName() + "_" + name.replaceAll(" ", "-")).toLowerCase();
        }
    }

    public String getMarkerid() {
        return markerid;
    }

    public final String getName() {
        return name;
    }

    public final World getWorld() {
        return world;
    }

    public final String getDescription() {
        return description;
    }

    public final Location getPos1() {
        return pos1;
    }

    public final Location getPos2() {
        return pos2;
    }

    public final AreaStyle getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "dynmap area named " + name + "in world " + world;
    }
}

