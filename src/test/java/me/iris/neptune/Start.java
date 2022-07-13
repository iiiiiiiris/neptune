package me.iris.neptune;

import me.iris.neptune.systems.*;

import java.lang.reflect.InvocationTargetException;

public class Start {
    public static void main(String[] args) {
        EventSystem sys = new VerboseEventSystem(System.out).setBrute(true);
        //EventSystem sys = new BruteEventSystem();
        ExampleListener listener = new ExampleListener();

        // You can use register(this) as well
        sys.register(listener);

        for (int i = 0; i < 3; i++) {
            try {
                sys.post(new ExampleEvent(i));
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sys.unregister(listener);
    }
}
