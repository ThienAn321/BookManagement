package com.learn.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.learn.service.dto.ErrorDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUserNotFoundException (UserNotFoundException ex) {
        ErrorDTO errorObject = new ErrorDTO();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUsernameAlreadyExist (UsernameAlreadyExist ex) {
        ErrorDTO errorObject = new ErrorDTO();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleTokenExpired (TokenExpiredException ex) {
        ErrorDTO errorObject = new ErrorDTO();
        errorObject.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);
    }
    
}
