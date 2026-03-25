package com.sneha.exceptions;

import com.sneha.Constants;
import com.sneha.errorservice.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleValidationException() {
        String exceptionMessage = Constants.ID_VALIDATION_EXCEPTION_MESSAGE;
        ValidationException exception = new ValidationException(exceptionMessage);

        ErrorResponse errorResponse = ErrorResponse.newBuilder()
                .setMessage(exceptionMessage)
                .build();

        ResponseEntity<ErrorResponse> responseEntity = handler.handleValidationException(exception);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

    @Test
    public void testHandleDuplicateUserException() {
        String exceptionMessage = Constants.DUPLICATE_USER_EXCEPTION_MESSAGE;
        DuplicateUserException exception = new DuplicateUserException();

        ErrorResponse errorResponse = ErrorResponse.newBuilder()
                .setMessage(exceptionMessage)
                .build();

        ResponseEntity<ErrorResponse> responseEntity = handler.handleDuplicateUserException(exception);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }


    @Test
    public void testHandleSystemException() {
        String exceptionMessage = Constants.SYSTEM_EXCEPTION_MESSAGE;
        InternalSystemException exception = new InternalSystemException(exceptionMessage);

        ErrorResponse errorResponse = ErrorResponse.newBuilder()
                .setMessage(exceptionMessage)
                .build();

        ResponseEntity<ErrorResponse> responseEntity = handler.handleSystemException(exception);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

}