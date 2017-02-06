package com.ps.touchcounter.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.touchcounter.R;
import com.ps.touchcounter.domain.TouchCounterLog;
import com.ps.touchcounter.domain.model.User;
import com.ps.touchcounter.ui.BaseActivity;
import com.ps.touchcounter.ui.touch.TouchCounterActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    private static final String TAG = TouchCounterLog.buildLogTag(LoginActivity.class.getSimpleName());


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    // Custom user login
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private Button mSignUpButton;
    TwitterLoginButton mTwitterButton;
    private String socialFlag = "Twitter";

    ViewGroup mMainContainer;
    LinearLayout signInContainer, signUpContainer;

    ILoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Containers
        mMainContainer = (ViewGroup) findViewById(R.id.main_container);
        signInContainer = (LinearLayout) findViewById(R.id.signIn_container);
        signUpContainer = (LinearLayout) findViewById(R.id.signUp_container);

        //Inject the views (Custom Login for SingIn and SingUp) in the respective containers
        signInContainer.addView(layoutInflater.inflate(R.layout.fragment_custom_login, mMainContainer, false));
        signInContainer.addView(layoutInflater.inflate(R.layout.fragment_divider_login, mMainContainer, false));
        signUpContainer.addView(layoutInflater.inflate(R.layout.fragment_sign_up, mMainContainer, false));
        signInContainer.addView(layoutInflater.inflate(R.layout.fragment_twitter_login, mMainContainer, false));
        signUpContainer.setVisibility(View.GONE);


        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mSignInButton = (Button) findViewById(R.id.button_sign_in);
        mSignUpButton = (Button) findViewById(R.id.button_sign_up);
        mTwitterButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        setFirebaseAuthListener();
        setTwitterCallbacks();

        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mTwitterButton.setOnClickListener(this);

        loginPresenter = new LoginPresenterImp(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (socialFlag.equalsIgnoreCase("Twitter")) {
            TouchCounterLog.Debug(TAG, " Result has come back :" + socialFlag);
            mTwitterButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_sign_in:
                //  loginPresenter.validateCredentialsAndSignIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                signIn();
                break;
            case R.id.button_sign_up:
                signUp();
                // loginPresenter.validateCredentialsAndSignUp(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.twitter_login_button:
                socialFlag = "Twitter";
                break;
        }

    }


    private void handleTwitterSession(TwitterSession session) {
        TouchCounterLog.Debug(TAG, "handleTwitterSession:" + session);
        showProgressDialog();

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        TouchCounterLog.Debug(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            TouchCounterLog.Warning(TAG, "signInWithCredential" + task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        updateUI(task.getResult().getUser());

                        hideProgressDialog();
                    }
                });
    }


    private void signOutTwitter() {
        mAuth.signOut();
        Twitter.logOut();
        updateUI(null);
    }


    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            TouchCounterLog.Debug(TAG, " User Name : " + user.getDisplayName() + " and User Id : " + user.getUid());
            Intent intent = new Intent(this, TouchCounterActivity.class);
            startActivity(intent);
        } else {
            TouchCounterLog.Debug(TAG, " Not successfully signed In");
        }
    }


    private void signIn() {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        TouchCounterLog.Debug(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        TouchCounterLog.Debug(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        TouchCounterLog.Debug(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(new Intent(LoginActivity.this, TouchCounterActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    // write to the firebase database
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onShowProgress() {
        showProgressDialog();
    }

    @Override
    public void onHideProgress() {
        hideProgressDialog();
    }

    @Override
    public void onEmailError() {
        mEmailField.setError("Required");
    }

    @Override
    public void onPasswordError() {
        mPasswordField.setError("Required");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        if (result) {
            startActivity(new Intent(LoginActivity.this, TouchCounterActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Login Fail", Toast.LENGTH_LONG).show();

        }
    }


    private void setFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    TouchCounterLog.Debug(TAG, "onAuthStateChanged:signedIn:" + firebaseUser.getUid());
                } else {
                    TouchCounterLog.Debug(TAG, "onAuthStateChanged:signedOut");
                }
                updateUI(firebaseUser);
            }
        };

    }

    private void setTwitterCallbacks() {
        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TouchCounterLog.Debug(TAG, "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                TouchCounterLog.Warning(TAG, "twitterLogin:failure"+ exception);
                updateUI(null);
            }
        });
    }


}