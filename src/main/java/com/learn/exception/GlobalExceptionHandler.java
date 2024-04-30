package com.learn.exception;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.learn.service.dto.response.ErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleDataNotFoundException(DataNotFoundException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setFieldName(ex.getField());
        error.setMessage(ex.getMessage());
        error.setMessageCode(ex.getMessageCode());
        error.setTimestamp(Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleDataUnauthorizedException(DataUnauthorizedException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setFieldName(ex.getField());
        error.setMessage(ex.getMessage());
        error.setMessageCode(ex.getMessageCode());
        error.setTimestamp(Instant.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleDataInvalidException(DataInvalidException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setFieldName(ex.getField());
        error.setMessage(ex.getMessage());
        error.setMessageCode(ex.getMessageCode());
        error.setTimestamp(Instant.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> map = new LinkedHashMap<>();
        Set<String> set = new HashSet<>();
        StringBuilder bd = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            set.add(fieldName);
        });
        for (String string : set) {
            bd.append(string + " , ");
        }
        bd.deleteCharAt(bd.length() - 2);
        map.put("fieldname", bd.toString().trim());
        map.put("message", bd.toString().trim() + " invalid");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldError = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            map.put(fieldError, errorMessage);
        });
        return map;
    }

}
