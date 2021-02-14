package com.technologyg.taxiii.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    FirebaseAuth mAuth;

    public AuthProvider(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> register(String email, String passw){
        return mAuth.createUserWithEmailAndPassword(email, passw);
    }
    public Task<AuthResult> login(String email, String passw){
        return mAuth.signInWithEmailAndPassword(email, passw);
    }
    public void logout(){
        mAuth.signOut();
    }

    public String getId(){
        return mAuth.getCurrentUser().getUid();
    }

    public boolean existSession(){
        boolean exist =false;
        if (mAuth.getCurrentUser() != null){
            exist = true;
        }
        return exist;
    }
}
