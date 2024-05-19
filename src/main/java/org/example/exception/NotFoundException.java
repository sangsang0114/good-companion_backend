package org.example.exception;

public class NotFoundException extends BusinessBaseException {
    public NotFoundException(String message, ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
