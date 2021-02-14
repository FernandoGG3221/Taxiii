package com.technologyg.taxiii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.activities.client.MapClientActivity;
import com.technologyg.taxiii.activities.client.RegisterUserActivity;
import com.technologyg.taxiii.activities.driver.MapDriverActivity;
import com.technologyg.taxiii.activities.driver.RegisterDriverActivity;

public class SelectOptionAuthActivity extends AppCompatActivity {
    Button mBtn_Register;
    TextInputEditText mEd_txt_email;
    TextInputEditText mEd_txt_passw;
    Button mBtn_login;

    FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    SharedPreferences mPreferences;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);
        mBtn_Register = findViewById(R.id.btn_Register);
        mBtn_login = findViewById(R.id.btn_IniciarSesion);
        mEd_txt_email = findViewById(R.id.txt_InputEmailLogin);
        mEd_txt_passw = findViewById(R.id.txt_InputPasswLogin);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        //initialize Preferences
        mPreferences = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mDialog = new SpotsDialog.Builder().setContext(SelectOptionAuthActivity.this).setMessage("Espere un segundo").build();

        mBtn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        String email = mEd_txt_email.getText().toString();
        String passw = mEd_txt_passw.getText().toString();

        if (!email.isEmpty() && !passw.isEmpty()){
            System.out.println("Los datos no están vacios");
            if (passw.length() >= 6){
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SelectOptionAuthActivity.this, "Iniciando sesión", Toast.LENGTH_SHORT).show();
                            String user = mPreferences.getString("user", "");
                            Intent intent;
                            if (user.equals("client")){
                                intent = new Intent(SelectOptionAuthActivity.this, MapClientActivity.class);
                                //Impide que vuelva a la pantalla del registro!
                            }else{
                                intent = new Intent(SelectOptionAuthActivity.this, MapDriverActivity.class);
                                //Impide que vuelva a la pantalla del registro!
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SelectOptionAuthActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
            }
            else{
                Toast.makeText(SelectOptionAuthActivity.this, "Minimo de 6 caracteres en la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(SelectOptionAuthActivity.this, "LLena los campos porfavor", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToRegister() {
        String typeUser = mPreferences.getString("user", "");
        if(typeUser.equals("client")){
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterUserActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterDriverActivity.class);
            startActivity(intent);
        }
    }
}