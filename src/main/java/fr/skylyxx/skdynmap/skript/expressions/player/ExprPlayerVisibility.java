package fr.skylyxx.skdynmap.skript.expressions.player;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Visibility of a Player")
@Description({"This expression returns the visibility of a player.\n",
        "It's a boolean (true/false)."})
@Since("1.0.2")
@Examples({
        "command /isvisible <player>:\n",
        "\ttrigger:\n",
        "\t\tset {_visibility} to arg-1's visibility\n",
        "\t\tif {_visibility} is true:\n",
        "\t\t\tsend \"%arg-1% is visible on the map!\"\n",
        "\t\telse:\n",
        "\t\t\tsend \"%arg-1% is not visible on the map\""
})
@RequiredPlugins("dynmap")
public class ExprPlayerVisibility extends SimplePropertyExpression<Player, Boolean> {

    static {
        register(ExprPlayerVisibility.class, Boolean.class,
                "[dynmap] visibility",
                "player"
        );
    }

    @Nullable
    @Override
    public Boolean convert(Player player) {
        return Util.getPlayerVisiblity(player);
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "player visibility";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            if (!(delta[0] instanceof Boolean)) {
                return;
            }
            Boolean isVisible = (Boolean) delta[0];
            for (Player player : getExpr().getArray(e)) {
                Util.setPlayerVisiblity(player, isVisible);
            }
        } else if (mode == Changer.ChangeMode.RESET) {
            for (Player player : getExpr().getArray(e)) {
                Util.setPlayerVisiblity(player, true);
            }
        }
    }
}
