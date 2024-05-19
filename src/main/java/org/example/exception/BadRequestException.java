package org.example.exception;

public class BadRequestException extends BusinessBaseException{
    public BadRequestException(String message, ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
