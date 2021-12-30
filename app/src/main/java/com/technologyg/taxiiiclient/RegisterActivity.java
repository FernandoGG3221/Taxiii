package com.technologyg.taxiiiclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyg.taxiiiclient.models.User;
import com.technologyg.taxiiiclient.providers.AuthProvider;
import com.technologyg.taxiiiclient.providers.UserProvider;

public class RegisterActivity extends AppCompatActivity {
    //EDIT TEXT
    private EditText mTxtName;
    private EditText mTxtFirstName;
    private EditText mTxtSecondName;
    private EditText mTxtEmail;
    private EditText mTxtPhone;
    private EditText mTxtPass;
    private EditText mTxtConfirmPass;
    //BUTTONS
    private Button mBtn_register_register;
    //FIREBASE INSTANCES
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    //PROVIDERS
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //INSTANCES
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        //Hace referencia a la base de datos de
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        System.out.println("name of data base: " + mDatabaseReference);

        //Find By ID
        mTxtName = findViewById(R.id.ed_txt_name);
        mTxtFirstName = findViewById(R.id.ed_txt_firstName);
        mTxtSecondName = findViewById(R.id.ed_txt_secondName);
        mTxtEmail = findViewById(R.id.ed_txt_email);
        mTxtPhone = findViewById(R.id.ed_txt_tel);
        mTxtPass = findViewById(R.id.ed_txt_pass);
        mTxtConfirmPass = findViewById(R.id.ed_txt_confirmPass);
        mBtn_register_register = findViewById(R.id.btn_register_register);

        //Click Listener
        mBtn_register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
        //String id = mAuthProvider.getId();
        //Syem.out.println("Id: " + id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void registration(){
        final String name = mTxtName.getText().toString();
        final String f_name = mTxtFirstName.getText().toString();
        final String s_name = mTxtSecondName.getText().toString();
        final String email = mTxtEmail.getText().toString();
        final String phone = mTxtPhone.getText().toString();
        final String pass = mTxtPass.getText().toString();
        final String pass2 = mTxtConfirmPass.getText().toString();

        if(!name.isEmpty() && !f_name.isEmpty() && !s_name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !pass.isEmpty() && !pass2.isEmpty()){
            //comprobation for pass length > 5
            if(pass.length()>5){
                //Comprobation coincide for pass and pass2
                if(pass.equals(pass2)){
                    completeRegister(email, name, f_name, s_name, phone, pass);
                }else {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RegisterActivity.this, "Añade 6 o más carácteres a tu contraseña", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(RegisterActivity.this, "Campos vacios, porfavor llena todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeRegister( final String emailU, final String nameU, final String f_nameU, final String s_nameU, final String phoneU, final String passU ){
        mAuthProvider.register(emailU, passU).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    System.out.println("id FireBaseAuth: "+ id);

                    //Instance of model User
                    User u = new User(id, emailU, nameU, f_nameU, s_nameU, phoneU, passU);
                    //
                    create(u);
                }else{
                    Toast.makeText(RegisterActivity.this, "Error el email ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void create(User user){
        mUserProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "BIENVENIDO", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, MapActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}