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

@Name("SkDynmap - Show a player from dynmap")
@Description("This effect let you show a player on the dynmap.")
@Since("1.0.2")
@Examples({
        "command /showme:",
        "\trigger:",
        "\t\tshow player on the dynmap",
        "\t\tsend \"&aYou are now visible !\""
})
@RequiredPlugins("dynmap")
public class EffShowPlayer extends Effect {

    static {
        Skript.registerEffect(EffShowPlayer.class,
                "show %player% (on|from) [the] [dyn]map",
                "make %player% visible (on|from) [the] [dyn]map"
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
        Util.setPlayerVisiblity(player.getSingle(e), true);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "show player on the dynmap";
    }


}
