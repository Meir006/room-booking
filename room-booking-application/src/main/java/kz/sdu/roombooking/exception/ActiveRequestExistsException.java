package kz.sdu.roombooking.exception;

public class ActiveRequestExistsException extends RuntimeException {

    private final Long requestId;

    public ActiveRequestExistsException(Long requestId) {
        super("User already has an active request: " + requestId);
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return requestId;
    }
}