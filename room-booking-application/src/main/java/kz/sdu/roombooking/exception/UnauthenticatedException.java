package kz.sdu.roombooking.exception;

public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {
        super("User is not authenticated");
    }

    public UnauthenticatedException(String message) {
        super(message);
    }
}