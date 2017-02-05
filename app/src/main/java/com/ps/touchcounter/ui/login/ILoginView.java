package com.ps.touchcounter.ui.login;

/**
 * Created by Azizur on 02/02/2017.
 */

public interface ILoginView {
    void onShowProgress();

    void onHideProgress();

    void onEmailError();

    void onPasswordError();

    void onLoginResult(Boolean result, int code);

}
