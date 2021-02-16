package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import fr.skylyxx.skdynmap.utils.types.DynmapMarker;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Make render")
@Description("Make the render of areas or markers or a global render")
@Since("1.1")
@Examples("command /renderall:\n" +
        "\ttrigger:\n" +
        "\t\tmake global render of skdynmap")
@RequiredPlugins("dynmap")
public class EffMakeRender extends Effect {

    static {
        Skript.registerEffect(EffMakeRender.class,
                "[make] render [of] %dynmapareas/dynmapmarkers%",
                "[make] global render [of [sk]dynmap]"
        );
    }

    private Expression<Object> itemExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            itemExpr = (Expression<Object>) exprs[0];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (itemExpr == null) {
            Util.renderAllAreas();
            Util.renderAllMarkers();
            return;
        }
        for (Object o : itemExpr.getAll(e)) {
            if (o instanceof DynmapArea) {
                Util.renderArea((DynmapArea) o);
            } else if (o instanceof DynmapMarker) {
                Util.renderMarker((DynmapMarker) o);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make render of " + itemExpr.toString(e, debug);
    }

}
