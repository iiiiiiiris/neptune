package me.iris.neptune.data.events;

public class CancellableEvent extends Event {
    private boolean cancelled;

    public CancellableEvent() { }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        // Cancel the event
        this.cancelled = true;
    }
}
