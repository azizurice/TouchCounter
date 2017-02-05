package com.ps.touchcounter.ui.login;



public interface ILoginPresenter {
    void validateCredentialsAndSignUp(String email, String password);
    void validateCredentialsAndSignIn(String email, String password);
    void onDestroy();
}
