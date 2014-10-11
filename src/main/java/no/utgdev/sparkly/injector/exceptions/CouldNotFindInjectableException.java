package no.utgdev.sparkly.injector.exceptions;

public class CouldNotFindInjectableException extends RuntimeException {
    public CouldNotFindInjectableException(Class<?> type) {
        super("Could not find injectable for " + type);
    }

    public CouldNotFindInjectableException(Class declaringClass, Class param) {
        super("Could not find injectable for " + param+" in "+declaringClass);
    }
}
