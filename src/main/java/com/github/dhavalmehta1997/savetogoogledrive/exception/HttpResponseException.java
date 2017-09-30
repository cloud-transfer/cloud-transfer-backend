package com.github.dhavalmehta1997.savetogoogledrive.exception;

import org.springframework.http.HttpStatus;

public class HttpResponseException extends RuntimeException {
    private HttpStatus status;

    public HttpResponseException(HttpStatus status) {
        this(status, null, null);
    }

    public HttpResponseException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public HttpResponseException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpResponseException(HttpStatus status, Throwable cause) {
        this(status, null, cause);
    }

    public HttpResponseException(HttpStatus status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }

    @Override
    public String toString() {
        return "HttpResponseException{" +
                "status=" + status +
                '}';
    }
}
