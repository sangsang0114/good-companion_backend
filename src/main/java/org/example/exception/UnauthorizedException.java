package org.example.exception;

public class UnauthorizedException extends BusinessBaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(String message, ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
