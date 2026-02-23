package com.sneha.service;

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

    public String registerUser(String name, String email) {
        if (name.isEmpty() || name == null) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        boolean userAlreadyPresent = userRepository.findByEmail(email).isPresent();

        if (userAlreadyPresent) {
            throw new IllegalArgumentException("User already exists");
        }

        UserDao updatedDao = userRepository.save(
                UserDao.builder().name(name).email(email).build()
        );

        return updatedDao.getId();
    }

    public boolean validateUser(String id) {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        return userRepository.findById(id).isPresent();
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();

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
    }

}
