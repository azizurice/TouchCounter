package com.ps.touchcounter.data.repository;

import android.content.SharedPreferences;

import com.ps.touchcounter.domain.model.User;
import com.ps.touchcounter.domain.repository.ISessionData;

import javax.inject.Inject;
import javax.inject.Singleton;


// We ignore it here as Firebase automatically persists the token (for this project). However, if any other server,
// we need it for saving token. That's why I have added here.
@Singleton
public class SaveSessionData implements ISessionData {
    private static final String EMAIL = "email";
    private static final String AUTH_TOKEN = "authorization_token";

    private final SharedPreferences sharedPreferences;

    @Inject
    public SaveSessionData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public User getCurrentUser() {
        if (sharedPreferences.contains(EMAIL) && sharedPreferences.contains(AUTH_TOKEN)) {
            User user = new User(sharedPreferences.getString(EMAIL, null));
            user.setAuthToken(sharedPreferences.getString(AUTH_TOKEN, null));
            return user;
        }
        return new User();
    }

    @Override
    public void setCurrentUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, user.getEmail());
        editor.putString(AUTH_TOKEN, user.getAuthToken());
        editor.apply();
    }

    @Override
    public void invalidateSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(EMAIL);
        editor.remove(AUTH_TOKEN);
        editor.apply();
    }
}