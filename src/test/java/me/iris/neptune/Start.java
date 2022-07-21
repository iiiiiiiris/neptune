package me.iris.neptune;

import me.iris.neptune.systems.*;

import java.lang.reflect.InvocationTargetException;

public class Start {
    public static void main(String[] args) {
        //EventSystem sys = new BasicEventSystem();
        //EventSystem sys = new BruteEventSystem();
        EventSystem sys = new VerboseEventSystem("main-event-system", System.out).setBrute(true);
        ExampleListener listener = new ExampleListener();

        // You can use register(this) as well
        sys.register(listener);

        for (int i = 0; i < 5; i++) {
            try {
                ExampleEvent event = new ExampleEvent(i);
                sys.post(event);

                // Have to add this in this example since we create a new instance of the event every number
                // By default the ExampleEvent has the NonCancellable annotation so this is ignored
                if (event.isCancelled())
                    break;
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sys.unregister(listener);
    }
}
