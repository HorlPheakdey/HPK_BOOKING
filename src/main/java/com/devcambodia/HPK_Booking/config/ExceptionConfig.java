package com.devcambodia.HPK_Booking.config;

import com.devcambodia.HPK_Booking.exception.*;
import com.devcambodia.HPK_Booking.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionConfig {
    private final CustomResponse customResponse;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return customResponse.error("Error Validation :{}"+errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandleNotFound.class)
    public ResponseEntity<Object> handleNotFound(HandleNotFound exception){
        return customResponse.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(HandleExpiration.class)
    public ResponseEntity<Object> handleExpiration(HandleExpiration exception){
        return customResponse.error(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(HandleInvalidFormat.class)
    public ResponseEntity<Object> handleInvalidFormat(HandleInvalidFormat exception){
        return customResponse.error(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(HandleUnsupport.class)
    public ResponseEntity<Object> handleUnsupport(HandleUnsupport exception){
        return customResponse.error(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(HandleDuplicate.class)
    public ResponseEntity<Object> handleDuplicate(HandleDuplicate exception){
        return customResponse.error(exception.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(HandleNotNull.class)
    public ResponseEntity<Object> handleNotNull(HandleNotNull exception){
        return customResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HandlePassword.class)
    public ResponseEntity<Object> handlePassword(HandlePassword exception){
        return customResponse.error(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
