package com.github.cloud_transfer.cloud_transfer_backend.exception;

public class InvalidAuthenticationTokenException extends APIException {

    public InvalidAuthenticationTokenException() {
        super();
    }

    public InvalidAuthenticationTokenException(String message) {
        super(message);
    }

    public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationTokenException(Throwable cause) {
        super(cause);
    }
}
