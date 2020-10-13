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

@Name("SkDynmap - Line Color")
@Description("This expression will return the line-color property of the provided style. (Return a text)")
@Examples({"set {_linecolor} to line-color of {_style}",
           "set line-color of {_style} to \"##2a6c80\""
})
@Since("1.0-beta02")
public class ExprLineColor extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprLineColor.class, String.class, ExpressionType.SIMPLE, "(line-color|line color) of %areastyle%");
    }

    private Expression<AreaStyle> style;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        style = (Expression<AreaStyle>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (style.getSingle(e) == null) {
            return null;
        }
        String lineColor = style.getSingle(e).getLineColor();
        return new String[]{lineColor};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "line-color of areastyle";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        } else {
            Util.log("Style properties can only be reset or set !", Level.SEVERE);
            return null;
        }
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            style.getSingle(e).setLineColor((String) delta[0]);
        } else if (mode == Changer.ChangeMode.RESET) {
            style.getSingle(e).setLineColor(Util.getDefaultStyle().getLineColor());
        } else {
            Util.log("Style properties can only be reset or set !", Level.SEVERE);
        }
    }
}
