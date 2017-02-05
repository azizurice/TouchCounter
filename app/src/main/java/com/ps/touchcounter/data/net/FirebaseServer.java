package com.ps.touchcounter.data.net;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.touchcounter.ui.login.ILoginInteractor;

/**
 * Created by Azizur on 04/02/2017.
 */

public class FirebaseServer implements ILoginInteractor.OnLoginFirebase {
    private static FirebaseServer instance=null;

    // Authentication with firebase backend services
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
    }

    @Override
    public void singIn(String email, String password) {

    }

    @Override
    public void signUp(String email, String password) {

    }
}
