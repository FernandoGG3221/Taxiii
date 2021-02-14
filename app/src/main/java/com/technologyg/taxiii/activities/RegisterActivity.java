package com.technologyg.taxiii.activities;


import androidx.appcompat.app.AppCompatActivity;

//import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.technologyg.taxiii.R;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.technologyg.taxiii.models.User;

public class RegisterActivity extends AppCompatActivity {

   // SharedPreferences mPreferences;
    //FirebaseAuth mAuth;
    //DatabaseReference mDataBase;

    Button mButton;
    Button mBtnRegister;
    TextInputEditText mTxt_email;
    TextInputEditText mTxt_name;
    TextInputEditText mTxt_firstName;
    TextInputEditText mTxt_secondName;
    TextInputEditText mTxt_tel;
    TextInputEditText mTxt_pass;
    TextInputEditText mTxt_passConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mAuth = FirebaseAuth.getInstance();
        //mDataBase = FirebaseDatabase.getInstance().getReference();

       // mPreferences = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        //Toast.makeText(RegisterActivity.this, "Datos correctos", Toast.LENGTH_SHORT).show();

        mButton = findViewById(R.id.btn_register_register);
        mTxt_email = findViewById(R.id.ed_txt_email_register);
        mTxt_name = findViewById(R.id.ed_txt_name_register);
        mTxt_firstName = findViewById(R.id.ed_txt_nameFather_register);
        mTxt_secondName = findViewById(R.id.ed_txt_nameMoth_register);
        mTxt_tel = findViewById(R.id.ed_txt_phone_register);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //registerUser();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registerUser();
                
            }
        });

    }
    /*
    void registerUser(){
        final String email = mTxt_email.getText().toString();
        final String name = mTxt_name.getText().toString();
        final String apePat = mTxt_firstName.getText().toString();
        final String apeMat = mTxt_secondName.getText().toString();
        final String tel = mTxt_tel.getText().toString();
        final String pass = mTxt_pass.getText().toString();
        final String pass2 = mTxt_passConfirm.getText().toString();

        if(!email.isEmpty() && !name.isEmpty() && !apePat.isEmpty() && !apeMat.isEmpty() && !tel.isEmpty() && !pass.isEmpty() && !pass2.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Datos correctos", Toast.LENGTH_SHORT).show();
            if(pass.length() >= 6) {
                Toast.makeText(RegisterActivity.this, "Contraseña con más de 6 carácteres", Toast.LENGTH_SHORT).show();
                if(pass.equals(pass2)) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    /*mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id= mAuth.getCurrentUser().getUid();
                                //saveUser(id, email, name);
                            } else {
                                Toast.makeText(RegisterActivity.this, "No se pudo crear el usuario, prueba de nuevo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "La contraseña debe tener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Por favor llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


    /*void saveUser(String id, String email, String name){
        String selectedUser = mPreferences.getString("user", "");
        User user = new User();
        user.setEmail(email);
        user.setName(name);
       
        if(selectedUser.equals("driver")){
            mDataBase.child("Users").child("Drivers").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(selectedUser.equals("client")){
            mDataBase.child("Users").child("Clients").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/
}