package org.example.exception;

public class InternalServerErrorException extends BusinessBaseException {
    public InternalServerErrorException(String message, ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public InternalServerErrorException(ErrorCode errorCode) {
        super(errorCode);
    }
}
