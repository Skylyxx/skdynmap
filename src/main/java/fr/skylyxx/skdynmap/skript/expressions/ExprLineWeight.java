package fr.skylyxx.skdynmap.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.logging.Level;

@Name("SkDynmap - Line Weight")
@Description("This expression will return the line-weight property of the provided style. (Return an integer)")
@Examples({"set {_lineweight} to line-weight of {_style}",
           "set line-weight of {_style} to 4"
})
@Since("1.0-beta02")
public class ExprLineWeight extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprLineWeight.class, Integer.class, ExpressionType.SIMPLE, "(line-weight|line weight) of %areastyle%");
    }

    private Expression<AreaStyle> style;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        style = (Expression<AreaStyle>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Integer[] get(Event e) {
        if (style.getSingle(e) == null) {
            return null;
        }
        Integer lineWeight = style.getSingle(e).getLineWeight();
        return new Integer[]{lineWeight};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "line-weight of areastyle";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Number.class);
        } else {
            Util.log("Style properties can only be reset or set !", Level.SEVERE);
            return null;
        }
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            style.getSingle(e).setLineWeight(((Number) delta[0]).intValue());
        } else if (mode == Changer.ChangeMode.RESET) {
            style.getSingle(e).setLineWeight(Util.getDefaultStyle().getLineWeight());
        } else {
            Util.log("Style properties can only be reset or set !", Level.SEVERE);
        }
    }
}
