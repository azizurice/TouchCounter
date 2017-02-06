package com.ps.touchcounter.ui.login;

import android.text.TextUtils;

import com.ps.touchcounter.data.net.ConnectToFirebase;


public class LoginInteractorImp implements ILoginInteractor {
    ILoginInteractor.OnLoginFirebase connectToFirebase;

    public LoginInteractorImp() {
        connectToFirebase = ConnectToFirebase.getInstance();
    }

    @Override
    public void login(String email, String password, OnLoginFinishedListener listener) {

        boolean error = false;
        if (TextUtils.isEmpty(email)) {
            listener.onEmailError();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.onPasswordError();
            error = true;
            return;
        }
        if (!error) {
            if (connectToFirebase.signIn(email, password)) {
                listener.onSuccess(true);
            } else {
                listener.onSuccess(false);
            }

        }
    }


}
