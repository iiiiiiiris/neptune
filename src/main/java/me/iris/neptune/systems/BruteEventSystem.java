package me.iris.neptune.systems;

import me.iris.neptune.annotation.Subscribe;
import me.iris.neptune.data.Event;
import me.iris.neptune.data.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Basic event system; calls all listeners.
 */
public class BruteEventSystem extends BasicEventSystem {
    @Override
    public void register(Object cls) {
        // Check all methods for Subscribe annotation
        for (Method method : cls.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)) continue;

            //noinspection unchecked
            Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];
            if (!listeners.containsKey(event))
                listeners.put(event, new CopyOnWriteArrayList<>());

            // Add new listener class w/ class & method to event
            listeners.get(event).add(new Listener(cls, method));
        }
    }

    @Override
    public void post(Event event) throws InvocationTargetException, IllegalAccessException {
        // Get listener classes for event
        List<Listener> subs = listeners.get(event.getClass());
        if (subs == null) return;

        for (Listener method : subs) {
            // Ignore if the event is cancelled
            if (event.isCancelled()) break;

            boolean resetAccess = !method.getMethod().isAccessible();
            if (resetAccess)
                method.getMethod().setAccessible(true);

            // Invoke method
            method.getMethod().invoke(method.getListenerClass(), event);

            if (resetAccess)
                method.getMethod().setAccessible(false);
        }
    }
}
