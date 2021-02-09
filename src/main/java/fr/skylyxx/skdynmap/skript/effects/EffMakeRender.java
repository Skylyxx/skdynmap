package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffMakeRender extends Effect {

    static {
        Skript.registerEffect(EffMakeRender.class,
                "[make] render [of] %dynmapareas%"
        );
    }

    private Expression<DynmapArea> dynmapAreaExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        dynmapAreaExpression = (Expression<DynmapArea>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if(dynmapAreaExpression == null) {
            Util.renderAllAreas();
            return;
        }
        for(DynmapArea area : dynmapAreaExpression.getAll(e)) {
            Util.renderArea(area);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make render of " + (dynmapAreaExpression != null ? dynmapAreaExpression.toString(e, debug) : "all areas");
    }

}
