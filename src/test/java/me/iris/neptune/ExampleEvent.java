package me.iris.neptune;

import me.iris.neptune.annotation.NonCancellable;
import me.iris.neptune.data.Event;

// Never cancel the event
@NonCancellable
public class ExampleEvent extends Event {
    private final int num;

    public ExampleEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
