package com.ps.touchcounter.ui.login;

import android.widget.Toast;

/**
 * Created by Azizur on 02/02/2017.
 */

public class LoginPresenterImp implements ILoginPresenter,ILoginInteractor.OnLoginFinishedListener{

    private ILoginView loginView;
    private ILoginInteractor loginInteractor;

    public LoginPresenterImp(ILoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImp();
    }

    @Override
    public void validateCredentialsAndSignIn(String email, String password) {
        if (loginView != null) {
            loginView.onShowProgress();
        }

        loginInteractor.login(email, password, this);
    }

    @Override
    public void validateCredentialsAndSignUp(String email, String password) {
        if (loginView != null) {
            loginView.onShowProgress();
        }

        loginInteractor.login(email, password, this);
    }



    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onEmailError() {
        if (loginView != null) {
            loginView.onEmailError();
            loginView.onHideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.onPasswordError();
            loginView.onHideProgress();
        }
    }

    @Override
    public void onSuccess(boolean result) {
        if (loginView != null) {
            loginView.onHideProgress();
            loginView.onLoginResult(result,0);
          //  loginView.navigateToHome();
        }

    }


}



