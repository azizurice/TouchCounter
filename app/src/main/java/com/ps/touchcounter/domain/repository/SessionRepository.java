package com.ps.touchcounter.domain.repository;


import com.ps.touchcounter.domain.model.UserEntity;

public interface SessionRepository {
    UserEntity getCurrentUser();
    void setCurrentUser(UserEntity user);
    void invalidateSession();
}