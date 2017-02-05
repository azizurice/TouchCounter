package com.ps.touchcounter.domain.model;

/**
 * Created by Azizur on 03/02/2017.
 */

public class UserEntity {
    private String email;
    private String authToken;
    private String password;

    public UserEntity(){

    }
    public  UserEntity(String email){
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
