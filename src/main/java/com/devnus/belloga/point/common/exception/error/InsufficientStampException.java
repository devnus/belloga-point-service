package com.devnus.belloga.point.common.exception.error;

public class InsufficientStampException extends RuntimeException {
    public InsufficientStampException() {
        super();
    }
    public InsufficientStampException(String message, Throwable cause) {
        super(message, cause);
    }
    public InsufficientStampException(String message) {
        super(message);
    }
    public InsufficientStampException(Throwable cause) {
        super(cause);
    }
}
