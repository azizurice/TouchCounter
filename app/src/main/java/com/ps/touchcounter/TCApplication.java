//package com.ps.touchcounter;
//
//import android.app.Application;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
///**
// * Created by Azizur on 05/02/2017.
// */
//
//public class TCApplication extends Application {
//    public static FirebaseAuth mAuth;
//    public static FirebaseAuth.AuthStateListener mAuthListener;
//    public static DatabaseReference mDatabase;
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//    }
//
//}
