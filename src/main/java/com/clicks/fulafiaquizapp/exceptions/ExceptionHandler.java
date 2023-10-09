package com.clicks.fulafiaquizapp.exceptions;

import com.clicks.fulafiaquizapp.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.Collections.emptyMap;

@ControllerAdvice
@ResponseStatus
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse> handleNotFound(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(exception.getMessage(), emptyMap()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidParamsException.class)
    public ResponseEntity<CustomResponse> handleInvalidRequest(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse(exception.getMessage(), emptyMap()));
    }
}
