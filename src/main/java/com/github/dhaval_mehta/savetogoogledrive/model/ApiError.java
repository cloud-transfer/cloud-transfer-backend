package com.github.dhaval_mehta.savetogoogledrive.model;

import org.springframework.http.HttpStatus;

public class ApiError {

    int statusCode;
    String meaning;
    String message;
    String description;

    public ApiError(HttpStatus httpStatus, String message, String description) {
        statusCode = httpStatus.value();
        meaning = httpStatus.getReasonPhrase();
        this.message = message;
        this.description = description;
    }

    public ApiError(int statusCode, String meaning, String message, String description) {
        this.statusCode = statusCode;
        this.meaning = meaning;
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getStatus() {
        return HttpStatus.valueOf(statusCode);
    }
}
