package com.sneha;

public class UserValidationResponse {
    private boolean response;
    UserValidationResponse(boolean response){
        this.response = response;
    }

    public boolean isResponse() {
        return response;
    }
}
