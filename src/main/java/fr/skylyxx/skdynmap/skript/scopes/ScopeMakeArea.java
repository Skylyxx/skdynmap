package fr.skylyxx.skdynmap.skript.scopes;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.EffectSection;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Make Area")
@Description("This is a scope that allows you to create areas easily")
@Since("1.0.1")
@Examples("command /make:\n" +
        "\ttrigger:\n" +
        "\t\tmake area:\n" +
        "\t\t\tset name of area to \"My Area\"\n" +
        "\t\t\tset description of area to \"Using sections !\"\n" +
        "\t\t\tset locations of area to {pos-1} and {pos-2}\n" +
        "\t\t\tset {_style} to default area style\n" +
        "\t\t\tset line color of {_style} to \"##00FF00\"\n" +
        "\t\t\tset fill color of {_style} to \"##FFFF00\"\n" +
        "\t\t\tset style of area to {_style}\n" +
        "\t\tcreate area from last generated area")
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
        SectionNode topNode = (SectionNode) SkriptLogger.getNode();
        boolean hasNameSetter = false;
        boolean hasLocSetter = false;

        Pattern regexName1 = Pattern.compile("set (last )?(generated |created )?area's name to");
        Pattern regexName2 = Pattern.compile("set name of( last)?( generated| created)? area to");
        Pattern regexLoc1 = Pattern.compile("set (last )?(generated |created )?area's location(s)? to");
        Pattern regexLoc2 = Pattern.compile("set location(s)? of( last)?( generated| created)? area to");

        for (Node node : topNode) {
            Matcher matcherName1 = regexName1.matcher(node.getKey());
            Matcher matcherName2 = regexName2.matcher(node.getKey());
            if (matcherName1.find() || matcherName2.find()) {
                hasNameSetter = true;
                continue;
            }
            Matcher matcherLoc1 = regexLoc1.matcher(node.getKey());
            Matcher matcherLoc2 = regexLoc2.matcher(node.getKey());
            if (matcherLoc1.find() || matcherLoc2.find()) {
                hasLocSetter = true;
                continue;
            }
        }

        if (!hasNameSetter) {
            Skript.error("You have to define a name for your area in the \"make area\" scope !");
            return false;
        }
        if (!hasLocSetter) {
            Skript.error("You have to define locations for your area in the \"make area\" scope !");
            return false;
        }
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