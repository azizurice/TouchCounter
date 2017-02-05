package com.ps.touchcounter.domain.model;

/**
 * Created by Azizur on 02/02/2017.
 */

public class User implements IUser{
    String email;
    String password;

    public  User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
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
}

