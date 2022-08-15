package me.iris.neptune;

import me.iris.neptune.data.VerboseInfo;

public class Start {
    public static void main(String[] args) {
        // Create event bus with default settings
        //final EventBus bus = new EventBus();

        // Create custom event bus
        final EventBus bus = new EventBus.Builder()
                .setVerboseInfo(new VerboseInfo("main-bus", System.out))
                .canModifyAccess(true)
                .build();

        // Listener class
        ExampleListener listener = new ExampleListener();

        // You can use register(this) as well
        bus.register(listener);

        for (int i = 0; i < 5; i++) {
            ExampleEvent event = new ExampleEvent(i);
            bus.post(event);

            // Have to add this in this example since we create a new instance of the event every number
            // By default the ExampleEvent has the NonCancellable annotation so this is ignored
            if (event.isCancelled()) break;
        }

        bus.unregister(listener);
    }
}
