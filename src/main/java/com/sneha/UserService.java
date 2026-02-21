package com.sneha;

import java.util.List;

public class UserService {

    private UserDatabase userDatabase;
    private TimeUtil timeUtil;
    private IdGenerator idGenerator;

    UserService(IdGenerator idGenerator, TimeUtil timeUtil, UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
        this.idGenerator = idGenerator;
        this.timeUtil = timeUtil;
    }

    String registerUser(String name, String email) {
        if (name.isEmpty() || name == null) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        boolean userAlreadyPresent = userDatabase.validateUserByEmail(email);

        if (userAlreadyPresent == false) {
            throw new IllegalArgumentException("User already exists");
        }

        String id = idGenerator.generate();

        userDatabase.add(name, email, id, timeUtil.getCurrentTime(), timeUtil.getCurrentTime());

        return id;
    }

    boolean validateUser(String id) {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        return userDatabase.validateUserById(id);
    }

    List<User> getAllUser(){
      return  userDatabase.getUsers();
    }
}
