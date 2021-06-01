package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Logger;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Name("Create area")
@Description("Create an area and show it on the map")
@Since("1.1")
@Examples("command /createarea:\n" +
        "\ttrigger:\n" +
        "\t\tcreate area from new area named \"Hello World\" with description \"Hi !\" at {pos-1} and {pos-2}\n" +
        "\t\tsend \"Area created !\"")
@RequiredPlugins("dynmap")
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
