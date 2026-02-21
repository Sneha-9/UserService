package com.sneha;

import java.util.List;

public class GetUsersResponse {
    private List<User> users;

    GetUsersResponse(List<User> users){
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
