package com.technologyg.taxiii.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.models.Driver;
import com.technologyg.taxiii.providers.AuthProvider;
import com.technologyg.taxiii.providers.DriverProvider;

public class RegisterDriverActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    DriverProvider mDriverProvider;
    //views
    Button mBtn_Registrar;
    TextInputEditText mTxt_Email;
    TextInputEditText mTxt_name;
    TextInputEditText mTxt_1name;
    TextInputEditText mTxt_2name;
    TextInputEditText mTxt_Tel;
    TextInputEditText mTxt_pass;
    TextInputEditText mTxt_pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        mAuthProvider = new AuthProvider();
        mDriverProvider = new DriverProvider();

        mBtn_Registrar = findViewById(R.id.btn_regDriver);
        mTxt_Email = findViewById(R.id.txt_email_driver);
        mTxt_name = findViewById(R.id.txt_name_driver);
        mTxt_1name = findViewById(R.id.txt_apeP_driver);
        mTxt_2name = findViewById(R.id.txt_apeM_driver);
        mTxt_Tel = findViewById(R.id.txt_tel_driver);
        mTxt_pass = findViewById(R.id.txt_pass_driver);
        mTxt_pass2 = findViewById(R.id.txt_confPass_driver);


        mBtn_Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });
    }

    private void reg() {
        final String Email = mTxt_Email.getText().toString();
        final String name = mTxt_name.getText().toString();
        final String name1 = mTxt_1name.getText().toString();
        final String name2 = mTxt_2name.getText().toString();
        final String Tel = mTxt_Tel.getText().toString();
        final String pass = mTxt_pass.getText().toString();
        String pass2 = mTxt_pass2.getText().toString();

        if(!Email.isEmpty() && !name.isEmpty() && !name1.isEmpty() && !name2.isEmpty() && !Tel.isEmpty() && !pass.isEmpty() && !pass2.isEmpty()) {
            Toast.makeText(this, "Datos llenos", Toast.LENGTH_SHORT).show();
            if(pass.length()>=6){
                Toast.makeText(this, "Comprobando coincidencia de contraseñas", Toast.LENGTH_SHORT).show();
                if (pass.equals(pass2)){
                    Toast.makeText(this, "Las contraseñas coinciden", Toast.LENGTH_SHORT).show();
                    System.out.println("Las contraseñas coinciden, \nHasta aquí va bien el código");
                    register(Email, name, name1, name2, Tel, pass);
                }
                else{
                    Toast.makeText(this, "Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"Minimo de 6 carácteres para la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
    }
    private void register(final String Email, final String name, final String name1, final String name2, final String tel,  final String pass){
        mAuthProvider.register(Email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(RegisterDriverActivity.this, "Creando usuario", Toast.LENGTH_SHORT).show();
                System.out.println("Task: " + task.toString());
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    System.out.println("Id: " + id);
                    Driver driver = new Driver(id, Email, name, name1, name2, tel, pass);
                    create(driver);

                }
                else{
                    Toast.makeText(RegisterDriverActivity.this, "Error al ejecutar el metodo de create", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void create(Driver driver){
        mDriverProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterDriverActivity.this, "Usuario Creado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterDriverActivity.this, MapDriverActivity.class);
                    //Impide que vuelva a la pantalla del registro!
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterDriverActivity.this, "Error al crear el cliente ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}