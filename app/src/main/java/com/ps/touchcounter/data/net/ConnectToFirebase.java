package com.ps.touchcounter.data.net;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.ps.touchcounter.TCApp;
import com.ps.touchcounter.domain.TouchCounterLog;
import com.ps.touchcounter.domain.model.User;
import com.ps.touchcounter.ui.login.ILoginInteractor;
import com.ps.touchcounter.ui.login.LoginActivity;

// We ignore this also as firebase and it callback methods are so happy working under activity.
// However, for other server, this is the right schekeleton to follow. That's why I kept it for future reference.
public class ConnectToFirebase implements ILoginInteractor.OnLoginFirebase {

    private static final String TAG = TouchCounterLog.buildLogTag(LoginActivity.class.getSimpleName());
    private static ConnectToFirebase instance = null;


    private boolean loginSucess = false;
    private boolean signUpSuccess = false;

    private ConnectToFirebase() {

    }

    public static ConnectToFirebase getInstance() {
        if (instance == null) {
            instance = new ConnectToFirebase();
        }
        return instance;

    }


    @Override
    public boolean signIn(String email, String password) {
        loginSucess = false;

        TCApp.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        TouchCounterLog.Debug(TAG, "signIn:onComplete:" + task.isSuccessful());
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
        signUpSuccess = false;
        TCApp.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        TouchCounterLog.Debug(TAG, "createUser:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            setSignUpResult(true);
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            setSignUpResult(false);
                        }
                    }
                });

        return signUpSuccess;
    }

    synchronized void setLogiInResult(boolean result) {
        loginSucess = result;
    }

    synchronized void setSignUpResult(boolean result) {
        signUpSuccess = result;
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
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
        TCApp.mDatabase.child("users").child(userId).setValue(user);
    }


}
