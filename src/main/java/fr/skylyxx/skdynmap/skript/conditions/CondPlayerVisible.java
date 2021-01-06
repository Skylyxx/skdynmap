package fr.skylyxx.skdynmap.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Is Player visible")
@Description("This condition let you check if a player is visible on the map.")
@Since("1.0.2")
@Examples({
        "command /togglevisibility:\n",
        "\ttrigger:\n",
        "\t\tif player isn't visible on the map:\n",
        "\t\t\tmake player not visible on the map\n",
        "\t\t\tsend \"&cYou are now hidden !\"\n",
        "\t\telse:\n",
        "\t\t\tmake player visible on dynmap\n",
        "\t\t\tsend \"&aYou are now visible !\""
})
@RequiredPlugins("dynmap")
public class CondPlayerVisible extends Condition {

    static {
        Skript.registerCondition(CondPlayerVisible.class,
                "%player% is visible [on [the] [dyn]map]",
                "%player% (isn't|is not) visible [on [the] [dyn]map]"
        );
    }

    private Expression<Player> player;
    private boolean isNegated;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        if (matchedPattern == 0) { // not negated
            isNegated = false;
        } else { // negated
            isNegated = true;
        }
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (player == null || player.getSingle(e) == null) {
            return false;
        }
        boolean result = Util.getPlayerVisiblity(player.getSingle(e));
        if (isNegated) {
            result = !result;
        }
        return result;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        String negation = "";
        if (isNegated) {
            negation = "n't";
        }
        return "player is" + negation + "visible";
    }
}
