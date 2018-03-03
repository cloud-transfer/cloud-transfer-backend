package com.github.dhaval_mehta.savetogoogledrive.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private HttpStatus status;

	public ApiException(HttpStatus status) {
		this(status, null, null);
	}

	public ApiException(HttpStatus status, String message) {
		this(status, message, null);
	}

	public ApiException(HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public ApiException(HttpStatus status, Throwable cause) {
		this(status, null, cause);
	}

	public ApiException(HttpStatus status, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ApiException{" + "status=" + status + '}';
	}
}
