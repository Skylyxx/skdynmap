package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffDeleteMarker extends Effect {

    static {
        Skript.registerEffect(EffDeleteMarker.class,
                "delete marker [from] %dynmapmarker%"
        );
    }

    private Expression<DynmapMarker> dynmapAreaExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        dynmapAreaExpr = (Expression<DynmapMarker>) exprs[0];
        return true;
    }


    @Override
    protected void execute(Event e) {
        DynmapMarker dynmapMarker = dynmapAreaExpr.getSingle(e);
        if (!Util.markerExist(dynmapMarker)) {
            return;
        }
        Util.unRenderMarker(dynmapMarker);
        dynmapMarker.deleteMarker();

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "delete area from " + dynmapAreaExpr.toString(e, debug);
    }
}
