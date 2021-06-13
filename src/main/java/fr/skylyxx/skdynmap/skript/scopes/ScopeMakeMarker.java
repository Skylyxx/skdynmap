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
import fr.skylyxx.skdynmap.utils.types.MarkerBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Make Marker")
@Description("This is a scope that allows you to create markers easily")
@Since("1.1")
@Examples("command /make:\n" +
        "\ttrigger:\n" +
        "\t\tmake marker:\n" +
        "\t\t\tset name of marker to \"Landmark\"\n" +
        "\t\t\tset location of marker to {pos-2}\n" +
        "\t\t\tset marker's icon to \"house\"\n" +
        "\t\tcreate marker from last generated marker")
@RequiredPlugins("dynmap")
public class ScopeMakeMarker extends EffectSection {

    public static MarkerBuilder lastMarker;

    static {
        Skript.registerCondition(ScopeMakeMarker.class, "make [new] [dynmap] marker");
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

        Pattern regexName1 = Pattern.compile("set (last )?(generated |created )?marker's name to");
        Pattern regexName2 = Pattern.compile("set name of( last)?( generated| created)? marker to");
        Pattern regexLoc1 = Pattern.compile("set (last )?(generated |created )?marker's location(s)? to");
        Pattern regexLoc2 = Pattern.compile("set location(s)? of( last)?( generated| created)? marker to");

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
            Skript.error("You have to define a name for your marker in the \"make marker\" scope !");
            return false;
        }
        if (!hasLocSetter) {
            Skript.error("You have to define location for your marker in the \"make marker\" scope !");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastMarker = new MarkerBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new dynmap marker";
    }

}