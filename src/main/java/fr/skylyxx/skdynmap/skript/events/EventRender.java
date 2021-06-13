package fr.skylyxx.skdynmap.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventRender extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    static {
        Skript.registerEvent("SkDynmap render", SimpleEvent.class, EventRender.class, "[[sk]dynmap] [map] render")
                .description("Called each time the SkDynmap layer is rendered. Cancellable event.")
                .since("1.2")
                .examples("on render:" +
                        "\tif {skdynmap-disable} is true:" +
                        "\t\tcancel event" +
                        "\t\tsend \"The render event has been cancelled !\" to console")
                .requiredPlugins("dynmap");
    }

    private boolean isCancelled;

    public EventRender() {
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
