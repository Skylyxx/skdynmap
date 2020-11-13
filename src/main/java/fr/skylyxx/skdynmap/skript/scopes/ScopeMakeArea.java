package fr.skylyxx.skdynmap.skript.scopes;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.skript.EffectSection;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Make Area")
@Description("This is a scope that allows you to create areas easily")
@Since("1.0.1")
@Examples({
        "command /createarea:\n",
        "\ttrigger:\n",
        "\t\tmake area:\n",
        "\t\t\tset area's name to \"My Area\"\n",
        "\t\t\tset area's description to \"My Description\"\n",
        "\t\t\tset area's world to world of player\n",
        "\t\t\tset area's style to default area style\n",
        "\t\t\tset area's location to {pos1} and {pos2}\n",
        "\t\tcreate the last area"
})
@RequiredPlugins("dynmap")
public class ScopeMakeArea extends EffectSection {

    public static AreaBuilder lastArea;

    static {
        Skript.registerCondition(ScopeMakeArea.class, "make [new] [dynmap] area");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastArea = new AreaBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new dynmap area";
    }

}
