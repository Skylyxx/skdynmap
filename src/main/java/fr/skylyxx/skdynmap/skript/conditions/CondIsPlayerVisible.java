package fr.skylyxx.skdynmap.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.dynmap.DynmapCommonAPI;

import javax.annotation.Nullable;

@Name("Is player visible")
@Description("Check if a player is visible on the map")
@Since("1.2")
@Examples("command /isvisible <player>:\n" +
        "\ttrigger:\n" +
        "\t\tif arg-1 is visible:\n" +
        "\t\t\tsend \"yes\"\n" +
        "\t\telse:\n" +
        "\t\t\tsend \"no\"")
@RequiredPlugins("dynmap")
public class CondIsPlayerVisible extends Condition {

    static {
        Skript.registerCondition(CondIsPlayerVisible.class,
                "%player% is visible",
                "%player% isn't visible"
        );
    }

    private Expression<Player> playerExpr;
    private boolean invert;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        invert = matchedPattern == 1;
        return true;
    }


    @Override
    public boolean check(Event e) {
        boolean bool = ((DynmapCommonAPI) SkDynmap.getINSTANCE().dynmap).getPlayerVisbility(playerExpr.getSingle(e).getName());
        if (invert)
            bool = !bool;
        return bool;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return playerExpr.toString(e, debug) + " is visible";
    }
}
