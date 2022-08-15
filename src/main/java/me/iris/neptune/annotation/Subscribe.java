package me.iris.neptune.annotation;

import me.iris.neptune.enums.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Subscribes a method to an event
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    EventPriority priority() default EventPriority.NORMAL;
}
