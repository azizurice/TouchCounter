package com.ps.touchcounter.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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
import com.ps.touchcounter.domain.model.User;
import com.ps.touchcounter.ui.BaseActivity;
import com.ps.touchcounter.ui.touch.TouchCounterActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;


public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    // Authentication with firebase backend services
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


        // 1. Twitter Auth setting
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));

        // 2. Initialize Auth and Database instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // 3. Start auth_state_listener for Twitter
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(firebaseUser);
            }
        };


        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mTwitterButton.setOnClickListener(this);


        //set callback for Twitter button
        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
                updateUI(null);
            }
        });


        //init
        loginPresenter = new LoginPresenterImp(this);
        //loginPresenter.setProgressBarVisiblity(View.INVISIBLE);
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
        Log.d(TAG, " Result has come back");
        if (socialFlag.equalsIgnoreCase("Twitter")) {
            Log.d(TAG, " Result has come back :" + socialFlag);
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
                //loginPresenter.validateCredentialsAndSignUp(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.twitter_login_button:
                socialFlag = "Twitter";
                break;
        }

    }


    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);
        showProgressDialog();

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
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
            Log.d(TAG, " User Name : " + user.getDisplayName()+ " and User Id : " + user.getUid());
            Intent intent = new Intent(this, TouchCounterActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG, " Not successfully signed in with Twitter authentication.");
        }
    }


    private void signIn() {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        //  loginPresenter.doLogin(mEmailField.getText().toString(), mPasswordField.getText().toString());
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
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
        Log.d(TAG, "signUp");
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
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
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
        // loginPresenter.onDestroy();
        super.onDestroy();
        //  hideProgressDialog();
    }


    @Override
    public void onShowProgress() {
        // progressBar.setVisibility(View.VISIBLE);
        showProgressDialog();
    }

    @Override
    public void onHideProgress() {
        //progressBar.setVisibility(View.GONE);
        hideProgressDialog();
    }

    @Override
    public void onEmailError() {
        mEmailField.setError("Required");
    }

    @Override
    public void onPasswordError() {
        //mPasswordField.setError(getString(R.string.password_error));
        mPasswordField.setError("Required");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        if (result) {
            Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, TouchCounterActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Login Fail", Toast.LENGTH_LONG).show();

        }
    }
}