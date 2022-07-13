package me.iris.neptune;

import me.iris.neptune.data.Event;

import java.lang.reflect.InvocationTargetException;

public abstract class EventSystem {
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
}
