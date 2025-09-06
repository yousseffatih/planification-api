package com.WALID.planification_api.exceptions;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.WALID.planification_api.playload.ErrorDetails;
import com.WALID.planification_api.playload.MessageResponse;
import com.WALID.planification_api.playload.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handlerResourceNoutFoundException(ResourceNotFoundException exception,WebRequest webRequest )
	{
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));

		return new ResponseEntity<>(errorDetails , HttpStatus.NOT_FOUND);
	}

	// for make  the api response shorter
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handlerGlobalException(Exception exception , WebRequest webRequest)
    {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<MessageResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<MessageResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new MessageResponse(fieldError.getDefaultMessage(),"warning"))
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
