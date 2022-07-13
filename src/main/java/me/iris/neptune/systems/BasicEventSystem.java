package me.iris.neptune.systems;

import me.iris.neptune.EventSystem;
import me.iris.neptune.annotation.Subscribe;
import me.iris.neptune.data.Event;
import me.iris.neptune.data.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Basic event system; only calls public/accessible listener methods.
 */
public class BasicEventSystem extends EventSystem {
    protected final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners;

    public BasicEventSystem() {
        this.listeners = new ConcurrentHashMap<>();
    }

    public void register(Object cls) {
        for (Method method : cls.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class))
                continue;

            //noinspection unchecked
            Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];
            if (!listeners.containsKey(event))
                listeners.put(event, new CopyOnWriteArrayList<>());

            listeners.get(event).add(new Listener(cls, method));
        }
    }

    public void unregister(Object cls) {
        for (CopyOnWriteArrayList<Listener> mth : listeners.values())
            mth.removeIf(method -> method.getListenerClass().equals(cls));
    }

    public void post(Event event) throws InvocationTargetException, IllegalAccessException {
        if (event.isCancelled())
            return;

        List<Listener> subs = listeners.get(event.getClass());
        if (subs == null)
            return;

        for (Listener method : subs)
            method.getMethod().invoke(method.getListenerClass(), event);
    }
}
