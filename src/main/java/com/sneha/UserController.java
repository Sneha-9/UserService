package com.sneha;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private TimeUtil timeUtil = new TimeUtil();
    private IdGenerator idGenerator = new IdGenerator();
    private UserDatabase userDatabase = new UserDatabase();
    private UserService userService = new UserService(idGenerator,timeUtil,userDatabase);

    @PostMapping(value = "/user/registration", produces = "application/json")
    UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest){
        String id = userService.registerUser(userRegisterRequest.getName(), userRegisterRequest.getEmail());

        return new UserRegisterResponse(id);
    }

    @PostMapping(value = "/user/validation", produces = "application/json")
    UserValidationResponse validateUser(UserValidationRequest userValidationRequest){
       boolean response =  userService.validateUser(userValidationRequest.getId());
        return  new UserValidationResponse(response);
    }

    @GetMapping(value = "/users", produces = "application/json")
    GetUsersResponse getAllUsers(){
        List<User> users = userService.getAllUser();
        return new GetUsersResponse(users);
    }

}
