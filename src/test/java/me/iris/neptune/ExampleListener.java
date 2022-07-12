package me.iris.neptune;

import me.iris.neptune.annotation.Subscribe;

public class ExampleListener {
    @Subscribe
    public void onExample(ExampleEvent event) {
        System.out.println(event.getNum());
    }
}
