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

public class ExprAreaWithID extends SimpleExpression<DynmapArea> {
    static {
        Skript.registerExpression(ExprAreaWithID.class, DynmapArea.class, ExpressionType.SIMPLE,
                "[dynmap] area with id %string%"
        );
    }

    private Expression<String> stringExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        stringExpression = (Expression<String>) exprs[0];
        return true;
    }


    @Nullable
    @Override
    protected DynmapArea[] get(Event e) {
        return new DynmapArea[]{Util.getAreaByID(stringExpression.getSingle(e))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends DynmapArea> getReturnType() {
        return DynmapArea.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "dynmap area with id \"" + stringExpression.getSingle(e) + "\"";
    }
}
