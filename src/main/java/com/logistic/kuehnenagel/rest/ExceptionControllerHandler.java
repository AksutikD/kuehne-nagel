package com.logistic.kuehnenagel.rest;

import com.logistic.kuehnenagel.error.ApiErrorResponse;
import com.logistic.kuehnenagel.error.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UrlPathHelper;

@RestControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(),
                new UrlPathHelper().getPathWithinApplication(request));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getMessage();

        if (ex.getFieldError() != null) {
            message = ex.getFieldError().getField() + " " + ex.getFieldError().getDefaultMessage();
        }

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), message,
                new UrlPathHelper().getPathWithinApplication(request));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
