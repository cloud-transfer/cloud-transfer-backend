package com.github.dhaval_mehta.savetogoogledrive.advice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.dhaval_mehta.savetogoogledrive.exception.ApiException;
import com.github.dhaval_mehta.savetogoogledrive.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
