package fr.skylyxx.skdynmap.skript.expressions.style;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("SkDynmap - Style of area")
@Description("This expression will return the style of the dynmap area provided. (Return a %areastyle%)")
@Examples({"set {_style} to style of area named \"My Area\" in world of player",
        "set style of area named \"My Area\" in world of player to default style"
})
@Since("1.0-beta02")
@RequiredPlugins("dynmap")
public class ExprStyleOfArea extends SimplePropertyExpression<DynmapArea, AreaStyle> {

    static {
        register(ExprStyleOfArea.class, AreaStyle.class,
                "[area(-| )]style",
                "dynmaparea"
        );
    }

    @Nullable
    @Override
    public AreaStyle convert(DynmapArea dynmapArea) {
        return dynmapArea.getStyle();
    }

    @Override
    public Class<? extends AreaStyle> getReturnType() {
        return AreaStyle.class;
    }

    @Override
    protected String getPropertyName() {
        return "area style";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(AreaStyle.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for (DynmapArea area : getExpr().getArray(e)) {
            area.setStyle((AreaStyle) delta[0]);
        }
    }
}
