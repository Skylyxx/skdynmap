package fr.skylyxx.skdynmap.skript.expressions.area.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - New area builder")
@Description("This expression allows you to obtain an Area Builder, which you can use to create yours areas")
@Since("1.0.1")
@Examples({
        "command /createarea:",
        "\ttrigger:",
        "\t\tset {_area} to new area named \"My Area\" in world of player between {pos1} and {pos2}",
        "\t\tcreate {_area}"
})
@RequiredPlugins("dynmap")
public class ExprAreaBuilder extends SimpleExpression<AreaBuilder> {

    static {
        Skript.registerExpression(ExprAreaBuilder.class, AreaBuilder.class, ExpressionType.SIMPLE,
                "[new] [dynmap] area named %string% in %world% at %locations%",
                "[new] [dynmap] area named %string% with description %string% in %world% at %locations%",
                "[new] [dynmap] area named %string% in %world% at %locations% with style %areastyle%",
                "[new] [dynmap] area named %string% with description %string% in %world% at %locations% with style %areastyle%"
        );
    }

    private Expression<String> name;
    private Expression<String> description;
    private Expression<World> world;
    private Expression<Location> locations;
    private Expression<AreaStyle> style;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        if (matchedPattern == 0) { //without desc / without style
            world = (Expression<World>) exprs[1];
            locations = (Expression<Location>) exprs[2];

        } else if (matchedPattern == 1) { //with desc / without style
            description = (Expression<String>) exprs[1];
            world = (Expression<World>) exprs[2];
            locations = (Expression<Location>) exprs[3];

        } else if (matchedPattern == 2) { //without desc / with style
            world = (Expression<World>) exprs[1];
            locations = (Expression<Location>) exprs[2];
            style = (Expression<AreaStyle>) exprs[3];

        } else if (matchedPattern == 3) { // with desc / with style
            description = (Expression<String>) exprs[1];
            world = (Expression<World>) exprs[2];
            locations = (Expression<Location>) exprs[3];
            style = (Expression<AreaStyle>) exprs[4];
        }
        return true;
    }

    @Nullable
    @Override
    protected AreaBuilder[] get(Event e) {
        AreaBuilder area = getAreaBuilder(e);
        return new AreaBuilder[]{area};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AreaBuilder> getReturnType() {
        return AreaBuilder.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        AreaBuilder area = getAreaBuilder(e);
        return area.toString();
    }

    private AreaBuilder getAreaBuilder(Event e) {
        String name = this.name.getSingle(e);
        String description = this.description != null ? this.description.getSingle(e) : null;
        World world = this.world.getSingle(e);
        AreaStyle style = this.style != null ? this.style.getSingle(e) : Util.getDefaultStyle();
        Location[] locations = this.locations.getAll(e);
        AreaBuilder areaBuilder = new AreaBuilder(name, description, world, style, locations);
        return areaBuilder;
    }
}
