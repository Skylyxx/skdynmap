package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExprChunksToLocations extends SimpleExpression<Location> {

    enum Direction {
        XPLUS,
        XMINUS,
        ZPLUS,
        ZMINUS
    }

    static {
        Skript.registerExpression(ExprChunksToLocations.class, Location.class, ExpressionType.SIMPLE,
                "corners of %chunks%"
        );
    }

    private Expression<Chunk> chunkExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        chunkExpr = (Expression<Chunk>) exprs[0];
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Nullable
    @Override
    protected Location[] get(Event e) {
        List<Location> locations = Util.getCorners(Arrays.asList(chunkExpr.getArray(e).clone()));
        return locations.toArray(new Location[0]);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "corners of " + chunkExpr.toString(e, debug);
    }
}
