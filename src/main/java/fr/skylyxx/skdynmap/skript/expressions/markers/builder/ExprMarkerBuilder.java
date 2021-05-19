package fr.skylyxx.skdynmap.skript.expressions.markers.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Marker builder")
@Description("The constructor for a future marker")
@Since("1.1")
@Examples("set {_builder} to new marker with id \"custom_id\" named \"Landmark\" at {pos-1}")
@RequiredPlugins("dynmap")
public class ExprMarkerBuilder extends SimpleExpression<MarkerBuilder> {

    static {
        Skript.registerExpression(ExprMarkerBuilder.class, MarkerBuilder.class, ExpressionType.SIMPLE,
                "new [dynmap] marker named %string% at %locations%", // 0: without id, without icon
                "new [dynmap] marker named %string% at %locations% with [icon] %string%", // 1: without id, with icon
                "new [dynmap] marker with id %string% named %string% at %locations%", // 2: with id, without icon
                "new [dynmap] marker with id %string% named %string% at %locations% with [icon] %string%" // 3: with id, with icon

        );
    }

    private Expression<String> idExpr;
    private Expression<String> nameExpr;
    private Expression<Location> locationsExpr;
    private Expression<String> iconExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            idExpr = null;
            nameExpr = (Expression<String>) exprs[0];
            locationsExpr = (Expression<Location>) exprs[1];
            iconExpr = null;
        } else if (matchedPattern == 1) {
            idExpr = null;
            nameExpr = (Expression<String>) exprs[0];
            locationsExpr = (Expression<Location>) exprs[1];
            iconExpr = (Expression<String>) exprs[2];
        } else if (matchedPattern == 2) {
            idExpr = (Expression<String>) exprs[0];
            nameExpr = (Expression<String>) exprs[1];
            locationsExpr = (Expression<Location>) exprs[2];
            iconExpr = null;
        } else if (matchedPattern == 3) {
            idExpr = (Expression<String>) exprs[0];
            nameExpr = (Expression<String>) exprs[1];
            locationsExpr = (Expression<Location>) exprs[2];
            iconExpr = (Expression<String>) exprs[3];
        }
        return true;
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends MarkerBuilder> getReturnType() {
        return MarkerBuilder.class;
    }

    @Nullable
    @Override
    protected MarkerBuilder[] get(Event e) {
        String name = nameExpr.getSingle(e);
        Location location = locationsExpr.getSingle(e);
        String id = idExpr != null ? idExpr.getSingle(e) : null;
        String iconID = iconExpr != null ? iconExpr.getSingle(e) : Config.DEFAULT_MARKER_ICON;
        MarkerBuilder markerBuilder = new MarkerBuilder(id, name, location, iconID);
        return new MarkerBuilder[]{markerBuilder};
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        StringBuilder result = new StringBuilder("new marker");
        if (idExpr != null) {
            result.append(" with id \"" + idExpr.toString(e, debug) + "\"");
        }
        result.append(" named \"" + nameExpr.toString(e, debug) + "\"");
        result.append(" at " + locationsExpr.toString(e, debug));
        if (iconExpr != null) {
            result.append(" with icon " + iconExpr.toString(e, debug));
        }
        return result.toString();
    }
}

