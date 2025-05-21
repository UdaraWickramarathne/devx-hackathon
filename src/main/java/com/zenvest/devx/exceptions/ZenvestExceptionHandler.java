package com.zenvest.devx.exceptions;
import com.zenvest.devx.responses.ZenvestResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;


@ControllerAdvice
public class ZenvestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ZenvestResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");


        return buildResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ZenvestResponse<Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        return buildResponseEntity("Invalid request format. Please check your JSON syntax and data types.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ZenvestResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        return buildResponseEntity("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ZenvestResponse<Object>> handleIllegalArgument(IllegalArgumentException ex){
        return buildResponseEntity("Bad Request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ZenvestResponse<Object>> handleException(Exception ex){
        return buildResponseEntity("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ZenvestResponse<Object>> handleRuntimeException(RuntimeException ex){
        return buildResponseEntity("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ZenvestResponse<Object>> buildResponseEntity(String message, HttpStatus status) {
        ZenvestResponse<Object> response = new ZenvestResponse<>(false, null, message);
        return  ResponseEntity.status(status).body(response);
    }
}
