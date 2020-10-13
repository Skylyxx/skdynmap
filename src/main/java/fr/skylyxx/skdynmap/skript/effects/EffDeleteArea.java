package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("SkDynmap - Delete an area")
@Description("This effect allow you to remove an area from the map")
@Examples("delete area named \"My Area\" in world of player")
@Since("1.0-beta02")
public class EffDeleteArea extends Effect {

    static {
        Skript.registerEffect(EffDeleteArea.class,
                "delete [dynmap] area (named|with name) %string% in %world%"
        );
    }

    private Expression<String> name;
    private Expression<World> world;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        world = (Expression<World>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String markerid = world.getSingle(e).getName() + "_" + name.getSingle(e).replace(" ", "-");
        markerid = markerid.toLowerCase();
        Util.deleteArea(markerid);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "delete dynmap area named " + name.getSingle(e) + " in world " + world.getSingle(e).getName();
    }
}
