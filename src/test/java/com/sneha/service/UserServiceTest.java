package com.sneha.service;

import com.sneha.exceptions.DuplicateUserException;
import com.sneha.exceptions.InternalSystemException;
import com.sneha.exceptions.ValidationException;
import com.sneha.model.UserDao;
import com.sneha.store.UserRepository;
import com.sneha.userservice.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    UserRepository userRepository = mock(UserRepository.class);

    @Test
    void returnExceptionWhenEmailIsEmpty() {
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).registerUser("sneha", ""));
    }
    @Test
    void returnExceptionWhenEmailIsNull() {
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).registerUser("sneha", null));
    }
    @Test
    void returnExceptionWhenNameIsNull() {
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).registerUser(null, "abc@gmail.com"));
    }
    @Test
    void returnExceptionWhenNameIsEmpty() {
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).registerUser("", "abc@gmail.com"));
    }

    @Test
    void returnDuplicateUserException(){
        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findByEmail("abc@gmail.com")).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(true);

        Assertions.assertThrows(DuplicateUserException.class,()-> new UserService(userRepository).registerUser("sneha", "abc@gmail.com"));
    }


    @Test
    void registerUserReturnsId() throws ValidationException, DuplicateUserException, InternalSystemException {
        String name = "sneha";
        String email = "abc@gmail.com";
        String id = "123";

        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findByEmail("abc@gmail.com")).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(false);

        UserDao requestDao = UserDao.builder()
                .email(email)
                .name(name)
                .build();

        UserDao responseDao = UserDao.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();

        when(userRepository.save(requestDao)).thenReturn(responseDao);

        String newUserId = new UserService(userRepository).registerUser("sneha","abc@gmail.com");

        Assertions.assertEquals("123", newUserId);

    }

    @Test
    void returnExceptionWhenDataBaseConnectionFailsWhileRegisteringUser(){

        when(userRepository.findByEmail("abc@gmail.com")).thenThrow();

        Assertions.assertThrows(Exception.class,()-> new UserService(userRepository).registerUser("sneha","abc"));
    }

    @Test
    void returnExceptionWhenIdIsNull(){
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).validateUser(null));
    }
    @Test
    void returnExceptionWhenIdIsEmpty(){
        Assertions.assertThrows(ValidationException.class,()-> new UserService(userRepository).validateUser(""));
    }


    @Test
    void returnFalseWhenUserIsInvalid() throws ValidationException, InternalSystemException {
        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findById("123")).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(false);

        Assertions.assertEquals(false, new UserService(userRepository).validateUser("123"));
    }

    @Test
    void returnTrueWhenUserIsValid() throws ValidationException, InternalSystemException {
        Optional mockedOptional = mock(Optional.class);

        when(userRepository.findById("123")).thenReturn(mockedOptional);

        when(mockedOptional.isPresent()).thenReturn(true);

        Assertions.assertEquals(true, new UserService(userRepository).validateUser("123"));
    }

    @Test
    void returnExceptionWhenDataBaseConnectionFailsWhileValidatingUser() {
        when(userRepository.findById("123")).thenThrow();

        Assertions.assertThrows(Exception.class,()-> new UserService(userRepository).validateUser("123"));
    }

    @Test
    void returnExceptionWhenDataBaseConnectionFailsWhileListingUser(){

        when(userRepository.findAll()).thenThrow();

        Assertions.assertThrows(Exception.class,()-> new UserService(userRepository).getAllUser());
    }


    @Test
    void returnListOfUsers() throws InternalSystemException {
        UserDao requestDao = UserDao.builder()
                .email("abc@gmail.com")
                .name("sneha")
                .id("123")
                .build();

        List<UserDao> userDaoList = new ArrayList<>();
        userDaoList.add(requestDao);

        when(userRepository.findAll()).thenReturn(userDaoList);

        List<User> userResponse  = new ArrayList<>();
        userResponse.add(User.newBuilder().setName("sneha").setEmail("abc@gmail.com").setId("123").build());

        List<User> result =  new UserService(userRepository).getAllUser();

        Assertions.assertEquals(userResponse, result);
    }




}