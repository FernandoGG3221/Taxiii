package com.technologyg.taxiiiclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.technologyg.taxiiiclient.providers.AuthProvider;

public class MainActivity extends AppCompatActivity {

    //BUTTONS
    private Button mBtnLogin;
    private Button mBtnRegister;
    //PROVIDERS
    private AuthProvider mAuthProvider;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Taxiii);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instances
        mAuthProvider = new AuthProvider();
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);

        //Click Listener
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuthProvider.existSession() == true){
            Intent i = new Intent(MainActivity.this, MapActivity.class);
            startActivity(i);
            finish();
        }
    }
}