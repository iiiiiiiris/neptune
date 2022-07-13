package me.iris.neptune.systems;

import me.iris.neptune.EventSystem;
import me.iris.neptune.annotation.Subscribe;
import me.iris.neptune.data.Event;
import me.iris.neptune.data.Listener;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class VerboseEventSystem extends EventSystem {
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners;
    private final PrintStream stream;
    private boolean brute;

    public VerboseEventSystem(PrintStream stream) {
        this.listeners = new ConcurrentHashMap<>();
        this.stream = stream;
    }

    /**
     * @param brute Enables access to non-public methods.
     */
    public VerboseEventSystem setBrute(boolean brute) {
        this.brute = brute;
        return this;
    }

    public void register(Object cls) {
        // Get methods to check for annotation
        final Method[] methods = brute ?
                cls.getClass().getDeclaredMethods() :
                cls.getClass().getMethods();

        // Check methods for Subscribe annotation
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Subscribe.class)) continue;

            //noinspection unchecked
            Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];
            if (!listeners.containsKey(event))
                listeners.put(event, new CopyOnWriteArrayList<>());

            // Add new listener class w/ class & method to event
            listeners.get(event).add(new Listener(cls, method));
        }

        stream.println("registered: " + cls.getClass().getName());
    }

    public void unregister(Object cls) {
        // Remove class if it's found
        for (CopyOnWriteArrayList<Listener> mth : listeners.values())
            mth.removeIf(method -> method.getListenerClass().equals(cls));

        stream.println("unregistered: " + cls.getClass().getName());
    }

    public void post(Event event) throws InvocationTargetException, IllegalAccessException {
        // Get listener classes for event
        List<Listener> subs = listeners.get(event.getClass());
        if (subs == null) return;

        for (Listener method : subs) {
            // Ignore if the event is cancelled
            if (event.isCancelled()) break;

            final boolean inaccessible = !method.getMethod().isAccessible();
            if (inaccessible && !brute) {
                stream.printf("%s> ignoring method \"%s\" from \"%s\". (not public)\n",
                        event.getClass().getName(), method.getMethod().getName(), method.getListenerClass().getClass().getName());
                continue;
            } else if (brute)
                method.getMethod().setAccessible(true);

            // Invoke method
            method.getMethod().invoke(method.getListenerClass(), event);
            if (brute)
                method.getMethod().setAccessible(false);

            stream.printf("%s> invoking method \"%s\" from class \"%s\".\n",
                    event.getClass().getName(), method.getMethod().getName(), method.getListenerClass().getClass().getName());
        }

        stream.println("posted event: " + event.getClass().getName());
    }
}
