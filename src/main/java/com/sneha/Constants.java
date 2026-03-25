package com.sneha;

public class Constants {

    public final static String DUPLICATE_USER_EXCEPTION_MESSAGE ="User with provided emailId already exists";
    public final static String NAME_VALIDATION_EXCEPTION_MESSAGE = "Name is either null or empty";
    public final static String EMAIL_VALIDATION_EXCEPTION_MESSAGE = "Email is either null or empty";
    public final static String ID_VALIDATION_EXCEPTION_MESSAGE = "Id is either null or empty";
    public final static String SYSTEM_EXCEPTION_MESSAGE = "Something went wrong , please try again";

    public final static String REGISTER_USER_PATH = "/user/registration";
    public final static String VALIDATE_USER_PATH = "/user/validation";
    public final static String GET_USERS_PATH ="/users";

    public final static String MEDIA_TYPE = "application/json";

    public final static String USER_DAO_TABLE_NAME = "users";
    public final static String USER_DAO_COLUMN_NAME_CREATED_AT = "createdat";
    public final static String USER_DAO_COLUMN_NAME_UPDATED_AT = "updatedat";



}
