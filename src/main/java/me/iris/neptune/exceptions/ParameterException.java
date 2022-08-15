package me.iris.neptune.exceptions;

import java.lang.reflect.Method;

public class ParameterException extends RuntimeException {
    public ParameterException(Method method) {
        super("A subscribed method doesn't contain an event.", new Throwable(method.getName()));
    }
}
