package me.iris.neptune.data;

import java.lang.reflect.Method;

public class Listener {
    private final Object listenerClass;
    private final Method method;

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
