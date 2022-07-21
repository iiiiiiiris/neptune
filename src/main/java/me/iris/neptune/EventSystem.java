package me.iris.neptune;

import me.iris.neptune.data.Event;
import me.iris.neptune.data.Listener;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class EventSystem {
    /**
     * Map containing events and their listening classes & method
     */
    protected final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners;

    /**
     * Constructor for event systems (DO NOT USE THIS TO INITIALIZE AN EVENT SYSTEM)
     */
    protected EventSystem() {
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
     * @throws InvocationTargetException Failed to invoke a listening method
     * @throws IllegalAccessException Can't access listening method
     */
    public abstract void post(Event event)
            throws InvocationTargetException, IllegalAccessException;

    /**
     * Automatically handles exceptions
     * @param event Event to post
     */
    public abstract void postHandle(Event event);
}
