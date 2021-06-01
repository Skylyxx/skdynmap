package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Logger;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Name("Create marker")
@Description("Create a marker and show it on the map")
@Since("1.1")
@Examples("command /createmarker:\n" +
        "\ttrigger:\n" +
        "\t\tset {_builder} to new marker named \"Marker\" at {pos-1} with icon \"house\"\n" +
        "\t\tcreate marker {_builder}\n" +
        "\t\tsend \"Marker created !\"")
@RequiredPlugins("dynmap")
public class EffCreateMarker extends Effect {

    static {
        Skript.registerEffect(EffCreateMarker.class,
                "create marker [from] %markerbuilder%"
        );
    }

    private Expression<MarkerBuilder> markerBuilderExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        markerBuilderExpr = (Expression<MarkerBuilder>) exprs[0];
        return true;
    }


    @Override
    protected void execute(Event e) {
        MarkerBuilder markerBuilder = markerBuilderExpr.getSingle(e);
        try {
            markerBuilder.createMarker();
        } catch (IOException | InvalidConfigurationException ioException) {
            Logger.severe("Error while creating a marker");
            ioException.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create area from " + markerBuilderExpr.toString(e, debug);
    }
}