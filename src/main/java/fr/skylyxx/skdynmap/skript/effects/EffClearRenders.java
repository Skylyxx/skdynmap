package fr.skylyxx.skdynmap.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffClearRenders extends Effect {

    static {
        Skript.registerEffect(EffClearRenders.class,
                "(wipe|clear) [sk]dynmap render[s]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        Util.unRenderAll();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }
}
