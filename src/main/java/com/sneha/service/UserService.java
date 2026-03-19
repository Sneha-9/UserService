package com.sneha.service;

import com.sneha.exceptions.DuplicateUserException;
import com.sneha.exceptions.InternalSystemException;
import com.sneha.exceptions.ValidationException;
import com.sneha.model.UserDao;
import com.sneha.store.UserRepository;
import com.sneha.userservice.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public String registerUser(String name, String email) throws DuplicateUserException, ValidationException, InternalSystemException {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Name is either null or empty");
        }

        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email is either null or empty");
        }

        boolean userAlreadyPresent;

        try {
            userAlreadyPresent = userRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            throw new InternalSystemException("Something went wrong , please try again");
        }

        if (userAlreadyPresent) {
            throw new DuplicateUserException();
        }

        UserDao updatedDao = userRepository.save(
                UserDao.builder().name(name).email(email).build()
        );

        return updatedDao.getId();
    }

    public boolean validateUser(String id) throws ValidationException, InternalSystemException {
        if (id == null || id.isEmpty()) {
            throw new ValidationException("Id is either null or empty");
        }

        try {
            boolean resp = userRepository.findById(id).isPresent();

            return resp;
        } catch (Exception e) {
            throw new InternalSystemException("Something went wrong , please try again");
        }

    }


    public List<User> getAllUser() throws InternalSystemException {
        List<User> users = new ArrayList<>();

        try {
            List<UserDao> fetchedUsers = userRepository.findAll();

            for (UserDao userDao : fetchedUsers) {
                users.add(
                        User.newBuilder()
                                .setId(userDao.getId())
                                .setEmail(userDao.getEmail())
                                .setName(userDao.getName())
                                .build()
                );
            }

            return users;
        } catch (Exception e) {
            throw new InternalSystemException("Something went wrong , please try again");
        }
    }

}
