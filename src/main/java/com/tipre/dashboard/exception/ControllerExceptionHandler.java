package com.tipre.dashboard.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return ErrorMessage.builder().statusCode(HttpStatus.NOT_FOUND.value()).timestamp(new Date())
				.message(ex.getMessage()).description(request.getDescription(false)).build();
	}

	@ExceptionHandler(ResourceBadRequestException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorMessage resourceBadRequestException(ResourceBadRequestException ex, WebRequest request) {
		return ErrorMessage.builder().statusCode(HttpStatus.BAD_REQUEST.value()).timestamp(new Date())
				.message(ex.getMessage()).description(request.getDescription(false)).build();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
		return ErrorMessage.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).timestamp(new Date())
				.message(ex.getMessage()).description(request.getDescription(false)).build();
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public ErrorMessage handleMaxSizeException(MaxUploadSizeExceededException ex) {
		return ErrorMessage
				.builder()
				.statusCode(HttpStatus.EXPECTATION_FAILED.value())
				.timestamp(new Date())
				.message("El archivo es demasiado grande.")
				.description(ex.getMessage())
				.build();
	}

}