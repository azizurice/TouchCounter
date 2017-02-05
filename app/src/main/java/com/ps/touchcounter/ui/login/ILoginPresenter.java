package com.ps.touchcounter.ui.login;

/**
 * Created by Azizur on 02/02/2017.
 */

public interface ILoginPresenter {
    void validateCredentialsAndSignUp(String email, String password);
    void validateCredentialsAndSignIn(String email, String password);
    void onDestroy();
}
