package com.github.cloud_transfer.cloud_transfer_backend.advice;

import com.github.cloud_transfer.cloud_transfer_backend.model.ApiError;
import com.github.cloud_transfer.cloud_transfer_backend.model.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class WebRestControllerAdvice {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiError> handleApiException(ApiException ex) {
		ApiError apiError = new ApiError(ex.getStatus(), ex.getMessage(), null);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiError> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), null);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception ex) {
		Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
