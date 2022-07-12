package me.iris.neptune;

import me.iris.neptune.data.Event;

public class ExampleEvent extends Event {
    private final int num;

    public ExampleEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
