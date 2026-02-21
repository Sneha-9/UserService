package com.sneha;

public class UserValidationRequest {
    private String id;
    UserValidationRequest(String id){
        this.id =id;
    }

    public String getId() {
        return id;
    }
}
