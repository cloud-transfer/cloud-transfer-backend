package com.github.dhavalmehta1997.savetogoogledrive.exception;

import org.springframework.http.HttpStatus;

public class HttpResponseException extends RuntimeException {
    HttpStatus status;

    public HttpResponseException(int statusCode) {
        this.status = HttpStatus.valueOf(statusCode);
    }

    public HttpResponseException(int statusCode, String message) {
        super(message);
        this.status = HttpStatus.valueOf(statusCode);
    }

    public HttpResponseException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.valueOf(statusCode);
    }

    public HttpResponseException(int statusCode, Throwable cause) {
        super(cause);
        this.status = HttpStatus.valueOf(statusCode);
    }

    public HttpResponseException(int statusCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = HttpStatus.valueOf(statusCode);
    }

    @Override
    public String toString() {
        return "HttpResponseException{" +
                "status=" + status +
                '}';
    }
}
