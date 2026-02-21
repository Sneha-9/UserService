package com.sneha;

public class User {

    private String name;
    private String email;
    private String id;

    User(String id,String name, String email){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
