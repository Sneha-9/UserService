package com.sneha;

import com.sneha.exceptions.*;
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

    @PostMapping(value = Constants.REGISTER_USER_PATH, consumes = Constants.MEDIA_TYPE, produces = Constants.MEDIA_TYPE)
    UserRegisterResponse registerUser(@RequestBody UserRegisterRequest userRegisterRequest) throws ValidationException, DuplicateUserException, InternalSystemException {
        String id = userService.registerUser(userRegisterRequest.getName(), userRegisterRequest.getEmail());

        return UserRegisterResponse.newBuilder().setId(id).build();
    }

    @PostMapping(value = Constants.VALIDATE_USER_PATH, consumes = Constants.MEDIA_TYPE, produces = Constants.MEDIA_TYPE)
    UserValidationResponse validateUser(@RequestBody UserValidationRequest userValidationRequest) throws ValidationException, InternalSystemException {
        boolean response = userService.validateUser(userValidationRequest.getId());

        return UserValidationResponse.newBuilder().setIsValid(response).build();
    }

    @GetMapping(value = Constants.GET_USERS_PATH, produces = Constants.MEDIA_TYPE)
    GetUsersResponse getAllUsers() throws InternalSystemException {
        List<User> users = userService.getAllUser();

        return GetUsersResponse.newBuilder().addAllUsers(users).build();
    }

}
