package me.iris.neptune.data;

import java.lang.reflect.Method;

public class Listener {
    /**
     * Class containing the method
     */
    private final Object listenerClass;

    /**
     * Subscribed method
     */
    private final Method method;

    /**
     * @param listenerClass Class containing the method
     * @param method Subscribed method
     */
    public Listener(Object listenerClass, Method method) {
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public Object getListenerClass() {
        return listenerClass;
    }

    public Method getMethod() {
        return method;
    }
}
