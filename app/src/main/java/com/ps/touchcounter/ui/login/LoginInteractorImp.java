package com.ps.touchcounter.ui.login;

import android.os.Handler;
import android.text.TextUtils;

import com.ps.touchcounter.data.net.FirebaseServer;



public class LoginInteractorImp implements ILoginInteractor {
    ILoginInteractor.OnLoginFirebase connectToFirebase;

    public LoginInteractorImp() {
        connectToFirebase = FirebaseServer.getInstance();
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
            if (connectToFirebase.signUp(email, password)) {
                listener.onSuccess(true);
            }else{
                listener.onSuccess(false);
            }

        }


//        // Mock login. I'm creating a handler to delay the answer a couple of seconds
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boolean error = false;
//                if (TextUtils.isEmpty(email)) {
//                    listener.onEmailError();
//                    error = true;
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    listener.onPasswordError();
//                    error = true;
//                    return;
//                }
//                if (!error) {
//                    listener.onSuccess();
//                }
//            }
//        }, 2000);

    }


}
