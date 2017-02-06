package com.ps.touchcounter.domain.model;


public class User implements IUser {
    String email;
    String password;
    private String authToken;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        this.email = email;
    }


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }



    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


