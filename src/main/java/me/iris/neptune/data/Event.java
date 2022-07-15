package me.iris.neptune.data;

import me.iris.neptune.annotation.NonCancellable;

public class Event {
    private final boolean cancellable;
    private boolean cancelled;

    public Event() {
        cancellable = !getClass().isAnnotationPresent(NonCancellable.class);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        // Check if the event can be cancelled
        if (!cancellable)
            return;

        // Cancel the event
        this.cancelled = true;
    }
}
