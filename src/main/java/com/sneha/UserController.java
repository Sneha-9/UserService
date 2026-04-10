package com.sneha;

import com.sneha.exceptions.*;
import com.sneha.service.UserService;
import com.sneha.userservice.*;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Supplier;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
//   private Metric metric;
   private MeterRegistry meterRegistry;

    @PostMapping(value = Constants.REGISTER_USER_PATH, consumes = Constants.MEDIA_TYPE, produces = Constants.MEDIA_TYPE)
    @Timed(value ="user_register_time_taken", description = "Time taken to execute register user operation")
    @Counted(value = "user_register_count", description = "Total number of user registration request")
    UserRegisterResponse registerUser(@RequestBody UserRegisterRequest userRegisterRequest) throws ValidationException, DuplicateUserException, InternalSystemException {
        String id = userService.registerUser(userRegisterRequest.getName(), userRegisterRequest.getEmail());
     //   metric.registerUser();

        Gauge.builder("active_sessions_count", (Supplier<Number>) () -> 0.0)
                .description("Current number of active user sessions")
                .register(meterRegistry);

        return UserRegisterResponse.newBuilder().setId(id).build();
    }

    @PostMapping(value = Constants.VALIDATE_USER_PATH, consumes = Constants.MEDIA_TYPE, produces = Constants.MEDIA_TYPE)
    UserValidationResponse validateUser(@RequestBody UserValidationRequest userValidationRequest) throws ValidationException, InternalSystemException {
        boolean response = userService.validateUser(userValidationRequest.getId());
       // metric.validateUser();
        return UserValidationResponse.newBuilder().setIsValid(response).build();
    }

    @GetMapping(value = Constants.GET_USERS_PATH, produces = Constants.MEDIA_TYPE)
    GetUsersResponse getAllUsers() throws InternalSystemException {
        List<User> users = userService.getAllUser();
        //metric.getUsers();
        return GetUsersResponse.newBuilder().addAllUsers(users).build();
    }

}
