package com.sneha;

import com.sneha.exceptions.DuplicateUserException;
import com.sneha.exceptions.InternalSystemException;
import com.sneha.exceptions.ValidationException;
import com.sneha.service.UserService;
import com.sneha.userservice.UserRegisterRequest;
import com.sneha.userservice.UserRegisterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    UserService userService = mock(UserService.class);
    Metric metric =mock(Metric.class);
    @Test
    void registerUserReturnId() throws ValidationException, DuplicateUserException, InternalSystemException {

       UserRegisterRequest userRegisterRequest = UserRegisterRequest.newBuilder().setEmail(TestConstant.email).setName(TestConstant.name).build();
        UserRegisterResponse userRegisterResponse = UserRegisterResponse.newBuilder().setId(TestConstant.id).build();
        when(userService.registerUser(userRegisterRequest.getName(),userRegisterRequest.getEmail())).thenReturn(TestConstant.id);

        Assertions.assertEquals(userRegisterResponse,new UserController(userService,metric).registerUser(userRegisterRequest) );
    }

    @Test
    void returnExceptionWhenNameIsNull() throws ValidationException, DuplicateUserException, InternalSystemException {

        UserRegisterRequest userRegisterRequest = mock(UserRegisterRequest.class);
        when(userService.registerUser(userRegisterRequest.getName(),userRegisterRequest.getEmail())).thenThrow(ValidationException.class);

        Assertions.assertThrows(ValidationException.class,()->new UserController(userService,metric).registerUser(userRegisterRequest) );
    }

    @Test
    void returnExceptionWhenUserIdAlreadyExist() throws ValidationException, DuplicateUserException, InternalSystemException {

        UserRegisterRequest userRegisterRequest = mock(UserRegisterRequest.class);
        when(userService.registerUser(userRegisterRequest.getName(),userRegisterRequest.getEmail())).thenThrow(DuplicateUserException.class);

        Assertions.assertThrows(DuplicateUserException.class,()->new UserController(userService,metric).registerUser(userRegisterRequest) );
    }

    @Test
    void returnExceptionWhenInternalSystemIsDown() throws ValidationException, DuplicateUserException, InternalSystemException {

        UserRegisterRequest userRegisterRequest = mock(UserRegisterRequest.class);
        when(userService.registerUser(userRegisterRequest.getName(),userRegisterRequest.getEmail())).thenThrow(InternalSystemException.class);

        Assertions.assertThrows(InternalSystemException.class,()->new UserController(userService, metric).registerUser(userRegisterRequest) );
    }

}