package me.iris.neptune;

import me.iris.neptune.data.events.CancellableEvent;

// Never cancel the event
public class ExampleEvent extends CancellableEvent {
    private final int num;

    public ExampleEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
