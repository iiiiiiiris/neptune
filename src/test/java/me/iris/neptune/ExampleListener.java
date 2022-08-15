package me.iris.neptune;

import me.iris.neptune.annotation.Subscribe;
import me.iris.neptune.enums.EventPriority;

public class ExampleListener {
    //@Subscribe
    @Subscribe(priority = EventPriority.HIGHEST)
    private void onExample(ExampleEvent event) {
        System.out.println(event.getNum());
        if (event.getNum() >= 3) {
            System.out.println("cancelled");
            event.cancel();
        }
    }
}
