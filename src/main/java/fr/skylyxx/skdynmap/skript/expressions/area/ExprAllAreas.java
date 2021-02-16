package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprAllAreas extends SimpleExpression<DynmapArea> {

    static {
        Skript.registerExpression(ExprAllAreas.class, DynmapArea.class, ExpressionType.SIMPLE,
                "all [dynmap] areas"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends DynmapArea> getReturnType() {
        return DynmapArea.class;
    }

    @Nullable
    @Override
    protected DynmapArea[] get(Event e) {
        return Util.getAllAreas();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all dynmap areas";
    }
}
