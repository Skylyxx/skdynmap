package fr.skylyxx.skdynmap.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Util;
import org.bukkit.World;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffDelArea extends Effect {

    static {
        Skript.registerEffect(EffDelArea.class,
                "delete [dynmap] area (named|with name) %string% in %world%");
    }

    private Expression<String> name;
    private Expression<World> world;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        world = (Expression<World>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "delete dynmap area named " + name.getSingle(e) + " in world " + world.getSingle(e).getName();
    }

    @Override
    protected void execute(Event e) {
        String markerid = world.getSingle(e).getName() + "_" + name.getSingle(e).replace(" ", "-");
        markerid = markerid.toLowerCase();
        Util.deleteArea(markerid);
    }
}
