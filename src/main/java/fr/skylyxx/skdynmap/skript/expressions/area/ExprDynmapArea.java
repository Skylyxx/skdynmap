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
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("SkDynmap - Dynmap Area")
@Description("This expression will search, and return the requested area. (Return a %dynmaparea%)")
@Examples("set {_area} to area named \"My Area\" in world of player")
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class ExprDynmapArea extends SimpleExpression<DynmapArea> {

    static {
        Skript.registerExpression(ExprDynmapArea.class, DynmapArea.class, ExpressionType.SIMPLE,
                "[dynmap] area (named|with name) %string% in %world%"
        );
    }

    private Expression<String> exprName;
    private Expression<World> exprWorld;
    private DynmapArea area;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprWorld = (Expression<World>) exprs[1];
        return true;
    }

    @Override
    protected DynmapArea[] get(Event e) {
        String name = exprName.getSingle(e);
        World world = exprWorld.getSingle(e);
        if (name == null || world == null)
            return new DynmapArea[0];
        area = Util.getArea(name, world);
        return new DynmapArea[]{area};
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
    public String toString(Event e, boolean debug) {
        try {
            return area.toString();
        } catch (NullPointerException err) {
            return null;
        }

    }
}
