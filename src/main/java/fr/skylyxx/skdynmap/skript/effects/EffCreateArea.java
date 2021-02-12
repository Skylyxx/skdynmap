package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Logger;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.io.IOException;

public class EffCreateArea extends Effect {

    static {
        Skript.registerEffect(EffCreateArea.class,
                "create area [from] %areabuilder%"
        );
    }

    private Expression<AreaBuilder> areaBuilderExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        areaBuilderExpr = (Expression<AreaBuilder>) exprs[0];
        return true;
    }


    @Override
    protected void execute(Event e) {
        AreaBuilder areaBuilder = areaBuilderExpr.getSingle(e);
        try {
            areaBuilder.createArea();
        } catch (IOException | InvalidConfigurationException ioException) {
            Logger.severe("Error while creating an area");
            ioException.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create area from " + areaBuilderExpr.toString(e, debug);
    }
}
