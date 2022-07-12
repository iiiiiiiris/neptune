package me.iris.neptune;

import java.lang.reflect.InvocationTargetException;

public class Start {
    public static void main(String[] args) {
        EventSystem sys = new EventSystem(true);
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
