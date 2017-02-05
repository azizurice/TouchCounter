package com.ps.touchcounter.ui.login;

/**
 * Created by Azizur on 02/02/2017.
 */

public interface ILoginInteractor {

    interface OnLoginFinishedListener {
        void onEmailError();

        void onPasswordError();

        void onSuccess(boolean result);
    }
    interface OnLoginFirebase{
        boolean signIn(String email, String password);
        boolean signUp(String email, String password);
    }

    void login(String email, String password, OnLoginFinishedListener listener);
}
