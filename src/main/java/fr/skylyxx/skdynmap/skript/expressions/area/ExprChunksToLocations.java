package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Name("Corners of chunks")
@Description("Get the locations from a list of chunks, to be used with locations of areas")
@Since("1.2")
@Examples("command /makechunks:\n" +
        "\ttrigger:\n" +
        "\t\tmake area:\n" +
        "\t\t\tset id of area to \"chunk_id\"\n" +
        "\t\t\tset name of area to \"Chunks\"\n" +
        "\t\t\tset locations of area to corners of {chunk::*}\n" +
        "\t\tcreate area from last area\n" +
        "\t\tsend \"Ok!\"")
@RequiredPlugins("dynmap")
public class ExprChunksToLocations extends SimpleExpression<Location> {

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