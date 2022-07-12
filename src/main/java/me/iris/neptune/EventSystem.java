package me.iris.neptune;

import me.iris.neptune.annotation.Subscribe;
import me.iris.neptune.data.Event;
import me.iris.neptune.data.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventSystem {
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners;

    private final boolean log;

    /**
     * EventSystem w/o logging
     */
    public EventSystem() {
        this.listeners = new ConcurrentHashMap<>();
        this.log = false;
    }

    /**
     * @param log Prints calls for debugging
     */
    public EventSystem(boolean log) {
        this.listeners = new ConcurrentHashMap<>();
        this.log = log;
    }

    /**
     * NOTE: SUBSCRIBED FUNCTIONS MUST BE PUBLIC
     * @param cls Listener class
     */
    public void register(Object cls) {
        for (Method method : cls.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class))
                continue;

            Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];
            if (!listeners.containsKey(event))
                listeners.put(event, new CopyOnWriteArrayList<>());

            listeners.get(event).add(new Listener(cls, method));
        }

        if (log)
            System.out.println("registered: " + cls.getClass());
    }

    /**
     * @param cls Listener class
     */
    public void unregister(Object cls) {
        for (CopyOnWriteArrayList<Listener> mth : listeners.values())
            mth.removeIf(method -> method.getListenerClass().equals(cls));

        if (log)
            System.out.println("unregistered: " + cls.getClass());
    }

    /**
     * @param event Event to post
     * @throws InvocationTargetException Failed to invoke a listening method
     * @throws IllegalAccessException Can't access listening method
     */
    public void post(Event event) throws InvocationTargetException, IllegalAccessException {
        if (event.isCancelled())
            return;

        List<Listener> subs = listeners.get(event.getClass());
        if (subs == null)
            return;

        for (Listener method : subs)
            method.getMethod().invoke(method.getListenerClass(), event);

        if (log)
            System.out.println("posted event: " + event.getClass());
    }
}
