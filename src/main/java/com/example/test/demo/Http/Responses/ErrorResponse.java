package com.example.test.demo.Http.Responses;

import org.springframework.http.HttpStatus;

public class ErrorResponse<T> extends ApiResponse {
    public ErrorResponse(Integer status, String message, HttpStatus httpStatus, Object data) {
        super(status, message, httpStatus, data);
    }

    public ErrorResponse(Integer status, String message, HttpStatus httpStatus) {
        super(status, message, httpStatus, null);
    }

    public ErrorResponse(Integer status, String message) {
        super(status, message, HttpStatus.BAD_REQUEST, null);
    }
}
