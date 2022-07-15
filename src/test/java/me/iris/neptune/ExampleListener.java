package me.iris.neptune;

import me.iris.neptune.annotation.Subscribe;

public class ExampleListener {
    @Subscribe
    private void onExample(ExampleEvent event) {
        /*
        Cancelling doesn't work in this example (natively)
        Read comment in Start.java
        */
        if (event.getNum() >= 3) {
            System.out.println("cancelled");
            event.cancel();
        }

        System.out.println(event.getNum());
    }
}
