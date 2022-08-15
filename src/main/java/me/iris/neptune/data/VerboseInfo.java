package me.iris.neptune.data;

import java.io.PrintStream;

public class VerboseInfo {
    private final String name;
    private final PrintStream stream;

    public VerboseInfo(String name, PrintStream stream) {
        this.name = name;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public PrintStream getStream() {
        return stream;
    }
}
