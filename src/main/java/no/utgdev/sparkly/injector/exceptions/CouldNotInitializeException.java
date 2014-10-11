package no.utgdev.sparkly.injector.exceptions;

public class CouldNotInitializeException extends RuntimeException {
    public CouldNotInitializeException(Class<?> c, Exception e) {
        super("Could not initialize " + c, e);
    }

    public CouldNotInitializeException(Class<?> c) {
        super("Could not initialize " + c);
    }

    public CouldNotInitializeException(String s) {
        super(s);
    }
}
