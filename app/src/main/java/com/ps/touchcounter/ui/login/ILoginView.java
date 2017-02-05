package com.ps.touchcounter.ui.login;


public interface ILoginView {
    void onShowProgress();

    void onHideProgress();

    void onEmailError();

    void onPasswordError();

    void onLoginResult(Boolean result, int code);

}
