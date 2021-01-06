package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Hide a player from dynmap")
@Description("This effect let you hide a player from the dynmap.")
@Since("1.0.2")
@Examples({
        "command /hideme:",
        "\trigger:",
        "\t\tmake player not visible on the map",
        "\t\tsend \"&cYou are now hidden !\""
})
@RequiredPlugins("dynmap")
public class EffHidePlayer extends Effect {

    static {
        Skript.registerEffect(EffHidePlayer.class,
                "hide %player% (on|from) [the] [dyn]map",
                "make %player% not visible (on|from) [the] [dyn]map"
        );
    }

    private Expression<Player> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (player == null || player.getSingle(e) == null) {
            return;
        }
        Util.setPlayerVisiblity(player.getSingle(e), false);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "hide player from the dynmap";
    }


}
