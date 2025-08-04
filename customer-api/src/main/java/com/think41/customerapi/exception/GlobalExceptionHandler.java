package com.think41.customerapi.exception;

import com.think41.customerapi.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(
            CustomerNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Customer Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Parameter",
                message,
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}