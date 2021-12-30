package com.technologyg.taxiiiclient.providers;

import android.system.ErrnoException;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AuthProvider {

    FirebaseAuth mAhut;

    //Initialize Firebase Auth
    public AuthProvider(){
        mAhut  = FirebaseAuth.getInstance();
    }

    //REGISTER IN DATABASE FIREBASE
    public Task<AuthResult>register(String email, String pass){
        return mAhut.createUserWithEmailAndPassword(email,pass);
    }

    //ACCESS TO DATABASE OF FIREBASE
    public Task<AuthResult> login(String email, String pass){
        return mAhut.signInWithEmailAndPassword(email, pass);
    }

    //LOGOUT
    public void logout(){
        mAhut.signOut();
    }

    //GET ID OF CURRENT USER
    public String getId(){
        final String uid = mAhut.getCurrentUser().getUid();
        return uid;
    }

    //SESSIONS EXIST? TO
     public boolean existSession() {
        boolean exist = false;
        if (mAhut.getCurrentUser() != null) {
            exist = true;
            System.out.println("Sesión iniciada");
        }
        return exist;
    }

}
