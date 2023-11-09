package com.learn.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.learn.service.dto.ObjectDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ObjectDTO> handleDataNotFoundException(DataNotFoundException ex) {
        ObjectDTO errorObject = new ObjectDTO();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ObjectDTO> handleDataUnauthorizedException(DataUnauthorizedException ex) {
        ObjectDTO errorObject = new ObjectDTO();
        errorObject.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ObjectDTO> handleDataInvalidException(DataInvalidException ex) {
        ObjectDTO errorObject = new ObjectDTO();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ObjectDTO> handleDataAlreadyExistException(DataAlreadyExistException ex) {
        ObjectDTO errorObject = new ObjectDTO();
        errorObject.setStatusCode(HttpStatus.CONFLICT.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ObjectDTO> handleUsernameAlreadyExist(SQLIntegrityConstraintViolationException ex) {
        ObjectDTO errorObject = new ObjectDTO();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage("Đăng kí thất bại ! Username hoặc email hoặc số điện thoại đã trùng");
        errorObject.setTimestamp(Instant.now());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", HttpStatus.BAD_REQUEST.value());
        map.put("message", "Data invalid");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            map.put(fieldName, errorMessage);
        });
        return map;
    }

}
