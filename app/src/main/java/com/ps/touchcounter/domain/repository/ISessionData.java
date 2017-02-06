package com.ps.touchcounter.domain.repository;


import com.ps.touchcounter.domain.model.User;

public interface ISessionData {
    User getCurrentUser();

    void setCurrentUser(User user);

    void invalidateSession();
}