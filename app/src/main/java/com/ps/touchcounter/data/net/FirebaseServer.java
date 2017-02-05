package com.ps.touchcounter.data.net;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.touchcounter.R;
import com.ps.touchcounter.domain.model.User;
import com.ps.touchcounter.ui.login.ILoginInteractor;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Azizur on 04/02/2017.
 */

public class FirebaseServer extends Activity implements ILoginInteractor.OnLoginFirebase {
    private static FirebaseServer instance=null;

    // Authentication with firebase backend services
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean loginSucess=false;
    private boolean signUpSuccess=false;

    private FirebaseServer(){

    }
    public static FirebaseServer getInstance(){
        if (instance==null) {
            instance = new FirebaseServer();
        }
        return instance;

    }

    public void setFirebaseParameters(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
      //  Resources.getSystem().getString(android.R.string.);
        // 1. Twitter Auth setting
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Resources.getSystem().getString(R.string.twitter_consumer_key),
                Resources.getSystem().getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));

    }

    @Override
    public boolean signIn(String email, String password) {
        loginSucess=false;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   //     Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                   //     hideProgressDialog();

                        if (task.isSuccessful()) {
                            setLogiInResult(true);
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            //Toast.makeText(LoginActivity.this, "Sign In Failed",Toast.LENGTH_SHORT).show();
                            setLogiInResult(false);
                        }
                    }
                });
        return loginSucess;

    }

    @Override
    public boolean signUp(String email, String password) {
        signUpSuccess=false;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                       /// hideProgressDialog();

                        if (task.isSuccessful()) {
                            setSignUpResult(true);
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            //Toast.makeText(LoginActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            setSignUpResult(false);
                        }
                    }
                });

        return signUpSuccess;
    }

    synchronized void setLogiInResult(boolean result){
        loginSucess=result;
    }
    synchronized void setSignUpResult(boolean result){
        signUpSuccess=result;
    }
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
       // startActivity(new Intent(LoginActivity.this, TouchCounterActivity.class));
       // finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    // write to the firebase database
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

//    private boolean validateForm() {
//        boolean result = true;
//        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
//            mEmailField.setError("Required");
//            result = false;
//        } else {
//            mEmailField.setError(null);
//        }
//
//        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
//            mPasswordField.setError("Required");
//            result = false;
//        } else {
//            mPasswordField.setError(null);
//        }
//
//        return result;
//    }



}
