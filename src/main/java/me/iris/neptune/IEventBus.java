package me.iris.neptune;

import me.iris.neptune.data.events.Event;
import me.iris.neptune.data.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class IEventBus {
    /**
     * Map containing events and their listening classes & method
     */
    protected final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners;

    /**
     * Semantic version
     */
    protected final String version = "2.0.1";

    /**
     * Constructor for event systems (DO NOT USE THIS TO INITIALIZE AN EVENT SYSTEM)
     */
    protected IEventBus() {
        this.listeners = new ConcurrentHashMap<>();
    }

    /**
     * @param cls Listener class
     */
    public abstract void register(Object cls);

    /**
     * @param cls Listener class
     */
    public abstract void unregister(Object cls);

    /**
     * @param event Event to post
     */
    public abstract void post(Event event);
}
