package fr.skylyxx.skdynmap.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventRender extends Event implements Cancellable {

    static {
        Skript.registerEvent("SkDynmap render", SimpleEvent.class, EventRender.class,
                "[[sk]dynmap] [map] render"
        );
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    public EventRender() {
        this.isCancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
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
