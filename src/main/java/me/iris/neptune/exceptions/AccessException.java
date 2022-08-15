package me.iris.neptune.exceptions;

public class AccessException extends RuntimeException {
    public AccessException(String accessMsg) {
        super("Failed to access listener method.", new IllegalAccessException(accessMsg));
    }
}
