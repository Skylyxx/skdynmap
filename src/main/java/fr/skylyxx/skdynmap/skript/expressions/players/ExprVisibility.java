package fr.skylyxx.skdynmap.skript.expressions.players;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.dynmap.DynmapCommonAPI;

import javax.annotation.Nullable;

public class ExprVisibility extends SimplePropertyExpression<Player, Boolean> {

    static {
        register(ExprVisibility.class, Boolean.class,
                "visibility",
                "player"
        );
    }


    @Override
    protected String getPropertyName() {
        return "visibility";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Nullable
    @Override
    public Boolean convert(Player player) {
        return ((DynmapCommonAPI)SkDynmap.getINSTANCE().dynmap).getPlayerVisbility(player.getName());
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if(mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (Player player : getExpr().getArray(e)) {
            switch(mode) {
                case SET: {
                    ((DynmapCommonAPI)SkDynmap.getINSTANCE().dynmap).setPlayerVisiblity(player.getName(), (Boolean) delta[0]);
                    break;
                }
                case RESET: {
                    ((DynmapCommonAPI)SkDynmap.getINSTANCE().dynmap).setPlayerVisiblity(player.getName(), (Boolean) true);
                    break;
                }
                default: break;
            }
        }
    }
}
