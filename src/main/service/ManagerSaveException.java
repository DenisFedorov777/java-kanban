package main.service;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ManagerSaveException(final String message) {
        super(message);
    }
}