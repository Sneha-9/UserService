package com.sneha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneha.errorservice.ErrorResponse;
import com.sneha.userservice.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
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

    //TODO: CONSIDER USING TEST CONSTANT OF BUILDER


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

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    void cleanDB(){
       jdbcTemplate.execute("TRUNCATE Table users");
    }

    @Test
    void shouldRegisterANewUser() {

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName(TestConstant.name)
                .setEmail(TestConstant.email)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
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
                .setEmail(TestConstant.email)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(Constants.NAME_VALIDATION_EXCEPTION_MESSAGE, errorResponse.getMessage());

    }

    @Test
    void shouldReturnErrorWhenUserAlreadyExists() {

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName(TestConstant.name)
                .setEmail(TestConstant.email)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        responseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(Constants.DUPLICATE_USER_EXCEPTION_MESSAGE, errorResponse.getMessage());

    }

    @Test
    void shouldReturnErrorWhenEmailIsInvalid(){
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.newBuilder().setEmail("").setName(TestConstant.name).build();

        RestTestClient.ResponseSpec responseSpec=  restTestClient.post().uri(Constants.REGISTER_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userRegisterRequest).exchange();

        responseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals(Constants.EMAIL_VALIDATION_EXCEPTION_MESSAGE,errorResponse.getMessage());
    }

    @Test
    void shouldValidateUserAsTrue(){

        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName(TestConstant.name)
                .setEmail(TestConstant.email)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        UserRegisterResponse registerResponse = responseSpec.expectBody(UserRegisterResponse.class)
                .returnResult()
                .getResponseBody();

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder().setId(registerResponse.getId()).build();


        RestTestClient.ResponseSpec validateresponseSpec = restTestClient.post()
                .uri(Constants.VALIDATE_USER_PATH)
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
                .setName(TestConstant.name)
                .setEmail(TestConstant.email)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerRequest)
                .exchange();

        UserRegisterResponse registerResponse = responseSpec.expectBody(UserRegisterResponse.class)
                .returnResult()
                .getResponseBody();

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder().setId(TestConstant.id).build();


        RestTestClient.ResponseSpec validateresponseSpec = restTestClient.post()
                .uri(Constants.VALIDATE_USER_PATH)
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
                .uri(Constants.VALIDATE_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userValidationRequest)
                .exchange();
        validateresponseSpec.expectStatus().is4xxClientError();

        ErrorResponse errorResponse = validateresponseSpec.expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals(Constants.ID_VALIDATION_EXCEPTION_MESSAGE,errorResponse.getMessage());
    }

    @Test
    void shouldListAllUsers(){
        //Register User Request
        UserRegisterRequest registerRequest = UserRegisterRequest.newBuilder()
                .setName(TestConstant.name)
                .setEmail(TestConstant.email)
                .build();

        //Make A call to register user
        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constants.REGISTER_USER_PATH)
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
                .uri(Constants.GET_USERS_PATH)
                .exchange();

        responseSpec.expectStatus().isOk();
        //Get User List Response

        GetUsersResponse getUsersResponse = listUsersResponseSpec.expectBody(GetUsersResponse.class).returnResult()
                .getResponseBody();

        Assertions.assertEquals(expected,getUsersResponse.getUsersList());
    }

}
