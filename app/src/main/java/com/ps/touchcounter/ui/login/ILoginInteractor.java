package com.ps.touchcounter.ui.login;


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
