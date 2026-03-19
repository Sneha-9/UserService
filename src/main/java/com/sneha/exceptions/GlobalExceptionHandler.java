package com.sneha.exceptions;

import com.sneha.errorservice.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return getResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {
         return getResponseEntity(ex, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(InternalSystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(InternalSystemException ex) {
        return getResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private ResponseEntity<ErrorResponse> getResponseEntity(Exception ex, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse error = ErrorResponse.newBuilder().setMessage(ex.getMessage()).build();

        return new ResponseEntity<>(error, headers, status);
    }

}
