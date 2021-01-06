package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - All areas")
@Description({"This expression returns a list with all the areas.\n",
        "You can use this expression to loop all areas."
})
@Since("1.0.2")
@Examples({
        "command /list-areas:",
        "\ttrigger:",
        "\t\tsend \"&eList of all areas:\"",
        "\t\tloop all areas:",
        "\t\t\tsend \"&e- &r%name of loop-area%\""
})
@RequiredPlugins("dynmap")
public class ExprAllAreas extends SimpleExpression<DynmapArea> {

    static {
        Skript.registerExpression(ExprAllAreas.class, DynmapArea.class, ExpressionType.SIMPLE,
                "all [the] [dynmap] areas"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected DynmapArea[] get(Event e) {
        return Util.getAllAreas();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class getReturnType() {
        return DynmapArea.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all the dynmap areas";
    }


}
