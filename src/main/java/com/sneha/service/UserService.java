package com.sneha.service;

import com.sneha.Constants;
import com.sneha.exceptions.DuplicateUserException;
import com.sneha.exceptions.InternalSystemException;
import com.sneha.exceptions.ValidationException;
import com.sneha.model.UserDao;
import com.sneha.store.UserRepository;
import com.sneha.userservice.User;
import io.micrometer.core.instrument.Counter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@AllArgsConstructor

public class UserService {

    private UserRepository userRepository;

    public String registerUser(String name, String email) throws DuplicateUserException, ValidationException, InternalSystemException {
        if (name == null || name.isEmpty()) {
            throw new ValidationException(Constants.NAME_VALIDATION_EXCEPTION_MESSAGE);
        }

        if (email == null || email.isEmpty()) {
            throw new ValidationException(Constants.EMAIL_VALIDATION_EXCEPTION_MESSAGE);
        }

        boolean userAlreadyPresent;

        try {
            userAlreadyPresent = userRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            log.error("Exception in user Service registering user flow while fetching record by email",e);
            throw new InternalSystemException(Constants.SYSTEM_EXCEPTION_MESSAGE);
        }

        if (userAlreadyPresent) {
            throw new DuplicateUserException();
        }

        UserDao updatedDao;
        try {
            updatedDao = userRepository.save(
                    UserDao.builder().name(name).email(email).build()
            );
            return updatedDao.getId();
        }
        catch (Exception e) {
            log.error("Exception in user Service registering user flow while inserting the record",e);
            throw new InternalSystemException(Constants.SYSTEM_EXCEPTION_MESSAGE);
        }

    }

    public boolean validateUser(String id) throws ValidationException, InternalSystemException {
        if (id == null || id.isEmpty()) {
            throw new ValidationException(Constants.ID_VALIDATION_EXCEPTION_MESSAGE);
        }

        try {
            boolean resp = userRepository.findById(id).isPresent();

            return resp;
        } catch (Exception e) {
            log.error("Exception in user Service registering user flow while fetching record by id",e);
            throw new InternalSystemException(Constants.SYSTEM_EXCEPTION_MESSAGE);
        }

    }


    public List<User> getAllUser() throws InternalSystemException {
        List<User> users = new ArrayList<>();
        List<UserDao> fetchedUsers;


        try {
            fetchedUsers = userRepository.findAll();
        }
        catch (Exception e) {
            log.error("Exception in user Service get all user user flow while fetching all records",e);
            throw new InternalSystemException(Constants.SYSTEM_EXCEPTION_MESSAGE);
        }

            for (UserDao userDao : fetchedUsers) {
                users.add(
                        User.newBuilder()
                                .setId(userDao.getId())
                                .setEmail(userDao.getEmail())
                                .setName(userDao.getName())
                                .build());
            }
            return users;
        }
    }


