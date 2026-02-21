package com.sneha;

public class UserRegisterRequest {
    private String name;
    private String email;

    UserRegisterRequest(String name, String email){
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
