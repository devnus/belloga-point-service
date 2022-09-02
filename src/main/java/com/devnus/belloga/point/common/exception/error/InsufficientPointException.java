package com.devnus.belloga.point.common.exception.error;

public class InsufficientPointException  extends RuntimeException {
    public InsufficientPointException() {
        super();
    }
    public InsufficientPointException(String message, Throwable cause) {
        super(message, cause);
    }
    public InsufficientPointException(String message) {
        super(message);
    }
    public InsufficientPointException(Throwable cause) {
        super(cause);
    }
}

