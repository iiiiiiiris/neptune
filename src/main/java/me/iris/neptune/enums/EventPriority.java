package me.iris.neptune.enums;

/**
 * What priority should the listening method have
 * Lowest is executed last & highest first
 */
public enum EventPriority {
    LOWEST(-2),
    LOW(-1),
    NORMAL(0),
    HIGH(1),
    HIGHEST(2);

    private final int priority;
    EventPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
