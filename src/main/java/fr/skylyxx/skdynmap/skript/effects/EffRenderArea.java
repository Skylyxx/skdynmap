package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffRenderArea extends Effect {

    static {
        Skript.registerEffect(EffRenderArea.class,
                "[make] render [of] %dynmaparea%",
                "[make] render [of] all [existing] areas"
        );
    }

    private Expression<DynmapArea> area;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            area = (Expression<DynmapArea>) exprs[0];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (area != null) {
            Util.renderArea(area.getSingle(e).getMarkerid());
        } else {
            Util.renderAllAreas();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        if (area != null) {
            return "render area " + area.toString(e, debug);
        } else {
            return "render all areas";
        }
    }
}
