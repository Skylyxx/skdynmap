package fr.skylyxx.skdynmap.skript.expressions.area.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.Config;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Area builder")
@Description("The constructor for a future area")
@Since("1.0.1")
@Examples("set {_builder} to new area with id \"custom_id\" named \"Blabla\" at ({pos-1}, {pos-2} and {pos-3}) with style {_style}")
@RequiredPlugins("dynmap")
public class ExprAreaBuilder extends SimpleExpression<AreaBuilder> {

    static {
        Skript.registerExpression(ExprAreaBuilder.class, AreaBuilder.class, ExpressionType.SIMPLE,
                "new [dynmap] area named %string% at %locations%", // 0: without id, without desc, without style
                "new [dynmap] area named %string% at %locations% with [style] %areastyle%", // 1: without id, without desc, with style
                "new [dynmap] area named %string% with description %string% at %locations%", // 2: without id, with desc, without style
                "new [dynmap] area named %string% with description %string% at %locations% with [style] %areastyle%", // 3: without id, with desc, with style
                "new [dynmap] area with id %string% named %string% at %locations%", // 4: with id, without desc, without style
                "new [dynmap] area with id %string% named %string% at %locations% with [style] %areastyle%", // 5: with id, without desc, with style
                "new [dynmap] area with id %string% named %string% with description %string% at %locations%", // 6: with id, with desc, without style
                "new [dynmap] area with id %string% named %string% with description %string% at %locations% with [style] %areastyle%" // 7: with id, with desc, with style
        );
    }

    private Expression<String> idExpr;
    private Expression<String> nameExpr;
    private Expression<String> descExpr;
    private Expression<Location> locationsExpr;
    private Expression<AreaStyle> styleExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            idExpr = null;
            nameExpr = (Expression<String>) exprs[0];
            descExpr = null;
            locationsExpr = (Expression<Location>) exprs[1];
            styleExpr = null;
        } else if (matchedPattern == 1) {
            idExpr = null;
            nameExpr = (Expression<String>) exprs[0];
            descExpr = null;
            locationsExpr = (Expression<Location>) exprs[1];
            styleExpr = (Expression<AreaStyle>) exprs[2];
        } else if (matchedPattern == 2) {
            idExpr = null;
            nameExpr = (Expression<String>) exprs[0];
            descExpr = (Expression<String>) exprs[1];
            locationsExpr = (Expression<Location>) exprs[2];
            styleExpr = null;
        } else if (matchedPattern == 3) {
            idExpr = null;
            nameExpr = (Expression<String>) exprs[0];
            descExpr = (Expression<String>) exprs[1];
            locationsExpr = (Expression<Location>) exprs[2];
            styleExpr = (Expression<AreaStyle>) exprs[3];
        } else if (matchedPattern == 4) {
            idExpr = (Expression<String>) exprs[0];
            nameExpr = (Expression<String>) exprs[1];
            descExpr = null;
            locationsExpr = (Expression<Location>) exprs[2];
            styleExpr = null;
        } else if (matchedPattern == 5) {
            idExpr = (Expression<String>) exprs[0];
            nameExpr = (Expression<String>) exprs[1];
            descExpr = null;
            locationsExpr = (Expression<Location>) exprs[2];
            styleExpr = (Expression<AreaStyle>) exprs[3];
        } else if (matchedPattern == 6) {
            idExpr = (Expression<String>) exprs[0];
            nameExpr = (Expression<String>) exprs[1];
            descExpr = (Expression<String>) exprs[2];
            locationsExpr = (Expression<Location>) exprs[3];
            styleExpr = null;
        } else if (matchedPattern == 7) {
            idExpr = (Expression<String>) exprs[0];
            nameExpr = (Expression<String>) exprs[1];
            descExpr = (Expression<String>) exprs[2];
            locationsExpr = (Expression<Location>) exprs[3];
            styleExpr = (Expression<AreaStyle>) exprs[4];
        }
        return true;
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AreaBuilder> getReturnType() {
        return AreaBuilder.class;
    }

    @Nullable
    @Override
    protected AreaBuilder[] get(Event e) {
        String name = nameExpr.getSingle(e);
        String desc = descExpr != null ? descExpr.getSingle(e) : "";
        Location[] locations = locationsExpr.getAll(e);
        AreaStyle areaStyle = styleExpr != null ? styleExpr.getSingle(e) : Config.DEFAULT_STYLE;
        String id = idExpr != null ? idExpr.getSingle(e) : name.toLowerCase().replaceAll(" ", "-") + "_" + locations[0].getWorld().getName().toLowerCase();
        AreaBuilder areaBuilder = new AreaBuilder(
                id,
                name,
                desc,
                locations,
                areaStyle
        );
        return new AreaBuilder[]{areaBuilder};
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        StringBuilder result = new StringBuilder("new area");
        if (idExpr != null) {
            result.append(" with id \"" + idExpr.toString(e, debug) + "\"");
        }
        result.append(" named \"" + nameExpr.toString(e, debug) + "\"");
        if (descExpr != null) {
            result.append(" with description \"" + descExpr.toString(e, debug) + "\"");
        }
        result.append(" at " + locationsExpr.toString(e, debug));
        if (styleExpr != null) {
            result.append(" with style " + styleExpr.toString(e, debug));
        }
        return result.toString();
    }
}

