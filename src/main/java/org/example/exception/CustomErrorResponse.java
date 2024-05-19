package org.example.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomErrorResponse {
    private String errorCode;
    private String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }
}
