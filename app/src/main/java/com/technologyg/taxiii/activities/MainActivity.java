package com.technologyg.taxiii.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.activities.client.MapClientActivity;
import com.technologyg.taxiii.activities.driver.MapDriverActivity;

public class MainActivity extends AppCompatActivity {
    Button mBtn_ImClient;
    Button mBtn_ImDriver;

    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        final SharedPreferences.Editor editor = mPreferences.edit();

        mBtn_ImClient = findViewById(R.id.btn_ImCliente);
        mBtn_ImDriver = findViewById(R.id.btn_ImDriver);

        mBtn_ImDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","driver");
                editor.apply();
                goToSigning();
            }
        });

        mBtn_ImClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","client");
                editor.apply();
                goToSigning();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){
            String user = mPreferences.getString("user", "");
            Intent intent;
            if (user.equals("client")){
                intent = new Intent(this, MapClientActivity.class);
                //Impide que vuelva a la pantalla del registro!
            }else{
                intent = new Intent(this, MapDriverActivity.class);
                //Impide que vuelva a la pantalla del registro!
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void goToSigning() {
            Intent intent = new Intent(MainActivity.this, SelectOptionAuthActivity.class);
            startActivity(intent);
    }
}