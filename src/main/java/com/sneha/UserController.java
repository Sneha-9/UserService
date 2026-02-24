package com.sneha;

import com.sneha.service.UserService;
import com.sneha.userservice.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping(value = "/user/registration", consumes = "application/json", produces = "application/json")
    UserRegisterResponse registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        String id = userService.registerUser(userRegisterRequest.getName(), userRegisterRequest.getEmail());

        return UserRegisterResponse.newBuilder().setId(id).build();
    }

    @PostMapping(value = "/user/validation", consumes = "application/json", produces = "application/json")
    UserValidationResponse validateUser(@RequestBody UserValidationRequest userValidationRequest) {
        boolean response = userService.validateUser(userValidationRequest.getId());

        return UserValidationResponse.newBuilder().setIsValid(response).build();
    }

    @GetMapping(value = "/users", produces = "application/json")
    GetUsersResponse getAllUsers() {
        List<User> users = userService.getAllUser();
        return GetUsersResponse.newBuilder().addAllUsers(users).build();
    }

}
