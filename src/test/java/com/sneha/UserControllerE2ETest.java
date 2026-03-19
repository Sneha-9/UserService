package com.sneha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneha.errorservice.ErrorResponse;
import com.sneha.userservice.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureRestTestClient
public class UserControllerE2ETest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("userservice")
            .withUsername("sneha")
            .withPassword("password")
            .withInitScript("test-init.sql");

    @Autowired
    private RestTestClient restTestClient;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldRegisterANewUser() {

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName("testName")
                .setEmail("test@name.com")
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        UserRegisterResponse registerResponse = responseSpec.expectBody(UserRegisterResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(registerResponse);
        Assertions.assertNotNull(registerResponse.getId());
    }

    @Test
    void shouldReturnErrorWhenNameIsInvalid() {

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName("")
                .setEmail("test@name.com")
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("Name is either null or empty", errorResponse.getMessage());

    }

    @Test
    void shouldReturnErrorWhenUserAlreadyExists() {

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName("test")
                .setEmail("test@name.com")
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("User with provided emailId already exists", errorResponse.getMessage());

    }

    @Test
    void shouldReturnErrorWhenEmailIsInvalid(){
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.newBuilder().setEmail("").setName("sneha").build();

        RestTestClient.ResponseSpec responseSpec=  restTestClient.post().uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userRegisterRequest).exchange();

        responseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals("Email is either null or empty",errorResponse.getMessage());
    }

    @Test
    void shouldValidateUserAsTrue(){

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName("test")
                .setEmail("test@name.com")
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        UserRegisterResponse registerResponse = responseSpec.expectBody(UserRegisterResponse.class)
                .returnResult()
                .getResponseBody();

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder().setId(registerResponse.getId()).build();


        RestTestClient.ResponseSpec validateresponseSpec = restTestClient.post()
                .uri("/user/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userValidationRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        UserValidationResponse validationResponseResponse = validateresponseSpec.expectBody(UserValidationResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(true, validationResponseResponse.getIsValid());

    }

    @Test
    void shouldValidateUserAsFalse(){

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName("test")
                .setEmail("test@name.com")
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        UserRegisterResponse registerResponse = responseSpec.expectBody(UserRegisterResponse.class)
                .returnResult()
                .getResponseBody();

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder().setId("123").build();


        RestTestClient.ResponseSpec validateresponseSpec = restTestClient.post()
                .uri("/user/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userValidationRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        UserValidationResponse validationResponseResponse = validateresponseSpec.expectBody(UserValidationResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(false, validationResponseResponse.getIsValid());
    }

    @Test
    void shouldThrowExceptionWhenIdIsInvalid(){
        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder().setId("").build();

        RestTestClient.ResponseSpec validateresponseSpec = restTestClient.post()
                .uri("/user/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userValidationRequest)
                .exchange();
        validateresponseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = validateresponseSpec.expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals("Id is either null or empty",errorResponse.getMessage());
    }

    @Test
    void shouldListAllUsers(){
        //Register User Request
        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName("test")
                .setEmail("test@name.com")
                .build();

        //Make A call to register user
        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        //Regist User Response
        UserRegisterResponse registerResponse = responseSpec.expectBody(UserRegisterResponse.class)
                .returnResult()
                .getResponseBody();
        //Build Expected User List
        List<User> expected= new ArrayList<>();
        expected.add(User.newBuilder().setName(registerRequest.getName()).setEmail(registerRequest.getEmail())
                .setId(registerResponse.getId()).build());
        //Make A Call To Get Users
        RestTestClient.ResponseSpec listUsersResponseSpec = restTestClient.get()
                .uri("/users")
                .exchange();

        responseSpec.expectStatus().isOk();
        //Get User List Response

        GetUsersResponse getUsersResponse = listUsersResponseSpec.expectBody(GetUsersResponse.class).returnResult()
                .getResponseBody();

        Assertions.assertEquals(expected,getUsersResponse.getUsersList());

    }



}
