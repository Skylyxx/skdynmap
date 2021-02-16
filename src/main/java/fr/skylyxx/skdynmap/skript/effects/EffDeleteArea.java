package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Delete area")
@Description("Delete an existent area")
@Since("1.1")
@Examples("command /deletearea <text>:\n" +
        "\ttrigger:\n" +
        "\t\tdelete area from area with id arg-1\n" +
        "\t\tsend \"Deleted !\"")
@RequiredPlugins("dynmap")
public class EffDeleteArea extends Effect {

    static {
        Skript.registerEffect(EffDeleteArea.class,
                "delete area [from] %dynmaparea%"
        );
    }

    private Expression<DynmapArea> dynmapAreaExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        dynmapAreaExpression = (Expression<DynmapArea>) exprs[0];
        return true;
    }


    @Override
    protected void execute(Event e) {
        DynmapArea dynmapArea = dynmapAreaExpression.getSingle(e);
        if (!Util.areaExist(dynmapArea)) {
            return;
        }
        Util.unRenderArea(dynmapArea);
        dynmapArea.deleteArea();

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "delete area from " + dynmapAreaExpression.toString(e, debug);
    }
}
