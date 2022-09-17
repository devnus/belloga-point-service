package com.devnus.belloga.point.common.exception.error;

public class NotFoundGiftIdException extends RuntimeException {
    public NotFoundGiftIdException() {
        super();
    }
    public NotFoundGiftIdException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundGiftIdException(String message) {
        super(message);
    }
    public NotFoundGiftIdException(Throwable cause) {
        super(cause);
    }
}
