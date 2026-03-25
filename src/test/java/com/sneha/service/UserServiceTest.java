package com.sneha.service;

import com.sneha.TestConstant;
import com.sneha.exceptions.DuplicateUserException;
import com.sneha.exceptions.InternalSystemException;
import com.sneha.exceptions.ValidationException;
import com.sneha.model.UserDao;
import com.sneha.store.UserRepository;
import com.sneha.userservice.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    UserRepository userRepository = mock(UserRepository.class);

    private static Stream<Arguments> inputData() {
        return Stream.of(
                Arguments.of(TestConstant.name, ""),
                Arguments.of(TestConstant.name, null),
                Arguments.of(null, TestConstant.email),
                Arguments.of("", TestConstant.email)
        );
    }

    @ParameterizedTest
    @MethodSource("inputData")
    void returnExceptionWhenInputIsInvalid(String name, String email) {
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).registerUser(name, email));
    }


    @Test
    void returnDuplicateUserException(){
        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findByEmail(TestConstant.email)).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(true);

        Assertions.assertThrows(DuplicateUserException.class,()-> new UserService(userRepository).registerUser(TestConstant.name, TestConstant.email));
    }


    @Test
    void registerUserReturnsId() throws ValidationException, DuplicateUserException, InternalSystemException {
        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findByEmail(TestConstant.email)).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(false);

        UserDao requestDao = UserDao.builder()
                .email(TestConstant.email)
                .name(TestConstant.name)
                .build();

        UserDao responseDao = UserDao.builder()
                .id(TestConstant.id)
                .email(TestConstant.email)
                .name(TestConstant.name)
                .build();

        when(userRepository.save(requestDao)).thenReturn(responseDao);

        String newUserId = new UserService(userRepository).registerUser(TestConstant.name,TestConstant.email);

        Assertions.assertEquals(TestConstant.id, newUserId);

    }

    @Test
    void returnExceptionWhenDataBaseConnectionFailsWhileRegisteringUser(){

        when(userRepository.findByEmail(TestConstant.email)).thenThrow();

        Assertions.assertThrows(Exception.class,()-> new UserService(userRepository).registerUser(TestConstant.name,TestConstant.email));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void returnExceptionWhenIdIsInvalid(String input){
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).validateUser(input));
    }


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void returnBooleanOnUserValidation(boolean input) throws ValidationException, InternalSystemException {
        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findById(TestConstant.id)).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(input);

        Assertions.assertEquals(input, new UserService(userRepository).validateUser(TestConstant.id));
    }


    @Test
    void returnExceptionWhenDataBaseConnectionFailsWhileValidatingUser() {
        when(userRepository.findById(TestConstant.id)).thenThrow();

        Assertions.assertThrows(Exception.class,()-> new UserService(userRepository).validateUser(TestConstant.id));
    }

    @Test
    void returnExceptionWhenDataBaseConnectionFailsWhileListingUser(){

        when(userRepository.findAll()).thenThrow();

        Assertions.assertThrows(Exception.class,()-> new UserService(userRepository).getAllUser());
    }


    @Test
    void returnListOfUsers() throws InternalSystemException {
        UserDao requestDao = UserDao.builder()
                .email(TestConstant.email)
                .name(TestConstant.name)
                .id(TestConstant.id)
                .build();

        List<UserDao> userDaoList = new ArrayList<>();
        userDaoList.add(requestDao);

        when(userRepository.findAll()).thenReturn(userDaoList);

        List<User> userResponse  = new ArrayList<>();
        userResponse.add(User.newBuilder().setName(TestConstant.name).setEmail(TestConstant.email).setId(TestConstant.id).build());

        List<User> result =  new UserService(userRepository).getAllUser();

        Assertions.assertEquals(userResponse, result);
    }

}