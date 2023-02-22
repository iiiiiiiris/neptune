package me.iris.neptune;

import me.iris.neptune.annotation.Subscribe;
import me.iris.neptune.data.VerboseInfo;
import me.iris.neptune.data.events.CancellableEvent;
import me.iris.neptune.data.events.Event;
import me.iris.neptune.data.Listener;
import me.iris.neptune.exceptions.AccessException;
import me.iris.neptune.exceptions.ParameterException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus extends IEventBus {
    private final VerboseInfo verbose;
    private final boolean modifyAccess;

    public EventBus() {
        this.verbose = null;
        this.modifyAccess = true;
    }

    public EventBus(VerboseInfo verbose, boolean modifyAccess) {
        this.verbose = verbose;
        this.modifyAccess = modifyAccess;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void register(Object cls) {
        // Get methods of class
        Method[] methods;
        if (modifyAccess)
            methods = cls.getClass().getDeclaredMethods();
        else
            methods = cls.getClass().getMethods();

        // Check if the class contains methods
        if (methods.length == 0) return;
        printf("register> searching for methods in %s", cls.getClass().getName());

        // Check methods for the "Subscribe" annotation
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Subscribe.class)) continue;
            if (method.getParameterCount() == 0)
                throw new ParameterException(method);

            // Set accessible if modify access is enabled
            if (!method.isAccessible() && modifyAccess) {
                method.setAccessible(true);
                printf("updated access of: " + method.getName());
            }

            // Get the event the method is subscribed to
            final Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];

            // Add to listeners map if it isn't already in it
            if (!listeners.containsKey(event))
                listeners.put(event, new CopyOnWriteArrayList<>());

            // Add method to event
            listeners.get(event).add(new Listener(cls, method));
            printf("registered: %s", method.getName());
        }
    }

    @Override
    public void unregister(Object cls) {
        listeners.values().forEach(listener ->
                listener.removeIf(method -> {
                    if (method.getListenerClass().equals(cls)) {
                        printf("unregistered: %s", cls.getClass().getName());
                        return true;
                    }

                    return false;
                }));
    }

    @Override
    public void post(Event event) {
        // Get listener classes for event
        List<Listener> subs = listeners.get(event.getClass());
        if (subs == null || subs.isEmpty()) return;

        // Sort by priority
        subs.sort(Comparator.comparingInt(listener ->
                listener.getMethod().getAnnotation(Subscribe.class).priority().getPriority()));

        printf("posting event: %s", event.getClass().getName());

        // Invoke listener methods
        for (Listener listener : subs) {
            // Check if the event is cancellable & if it has been cancelled
            if (event instanceof CancellableEvent &&
                    ((CancellableEvent)event).isCancelled()) break;

            // Invoke method
            try {
                listener.getMethod().invoke(listener.getListenerClass(), event);
                printf("%s> invoked: %s within %s", event.getClass().getName(), listener.getMethod().getName(),
                        listener.getListenerClass().getClass().getName());
            } catch (IllegalAccessException e) {
                throw new AccessException(e.getMessage());
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Prints a string to the provided stream (if provided)
     * @param fmt String format
     * @param args Arguments for string
     */
    private void printf(String fmt, Object... args) {
        if (verbose == null) return;

        verbose.getStream().printf("[%s] ", verbose.getName());
        verbose.getStream().printf(fmt, args);
        verbose.getStream().print('\n');
    }

    /**
     * Builds a custom event bus
     */
    public static class Builder {
        private VerboseInfo verboseInfo;
        private boolean modifyAccess;

        /**
         * Prints info related to events being posted
         *  ### FOR DEBUGGING DURING DEVELOPMENT ###
         */
        public Builder setVerboseInfo(VerboseInfo verboseInfo) {
            this.verboseInfo = verboseInfo;
            return this;
        }

        /**
         * Makes calls to private methods
         */
        public Builder canModifyAccess(boolean modifyAccess) {
            this.modifyAccess = modifyAccess;
            return this;
        }

        public EventBus build() {
            return new EventBus(verboseInfo, modifyAccess);
        }
    }
}
