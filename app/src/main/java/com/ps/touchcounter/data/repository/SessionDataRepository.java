package com.ps.touchcounter.data.repository;

import android.content.SharedPreferences;

import com.ps.touchcounter.domain.model.UserEntity;
import com.ps.touchcounter.domain.repository.SessionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Azizur on 04/02/2017.
 */

@Singleton
public class SessionDataRepository implements SessionRepository {
    private static final String EMAIL = "email";
    private static final String AUTH_TOKEN = "auth_token";

    private final SharedPreferences sharedPreferences;

    @Inject
    public SessionDataRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public UserEntity getCurrentUser() {
        if (sharedPreferences.contains(EMAIL) && sharedPreferences.contains(AUTH_TOKEN)) {
            UserEntity user = new UserEntity(sharedPreferences.getString(EMAIL, null));
            user.setAuthToken(sharedPreferences.getString(AUTH_TOKEN, null));
            return user;
        }
        return new UserEntity();
    }

    @Override
    public void setCurrentUser(UserEntity user) {
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