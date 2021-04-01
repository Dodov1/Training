package com.web.training.exceptions.handlers;

import com.web.training.exceptions.ErrorResponse;
import com.web.training.exceptions.ExceptionErrorModel;
import com.web.training.models.exceptionModels.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        ExceptionErrorModel apiError = new ExceptionErrorModel(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler(value = {HasTheSameTrainingException.class})
    protected ResponseEntity<Object> handleHasSameTraining(RuntimeException ex, WebRequest request) {
        ExceptionErrorModel apiError = new ExceptionErrorModel(CONFLICT);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(value = {InvalidSortingTypeException.class})
    protected ResponseEntity<Object> handleInvalidSortingTypeException(RuntimeException ex, WebRequest request) {
        ExceptionErrorModel apiError = new ExceptionErrorModel(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    protected ResponseEntity<Object> handleUserExistsException(RuntimeException ex, WebRequest request) {
        ExceptionErrorModel apiError = new ExceptionErrorModel(CONFLICT);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), CONFLICT, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details."
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }
}
