package com.technologyg.taxiii.providers;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.technologyg.taxiii.models.Token;

public class TokenProviders {
    DatabaseReference mDatabase;

    public TokenProviders(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tokens");
        System.out.println("Token provider: mDatabase = "+ mDatabase);
    }
    //Inserta el nuevo token en la base de datos
    public void create(final String idUser){
        if(idUser == null) return;
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                //devuelve el nuevo token de usuario
                Token token = new Token(instanceIdResult.getToken());
                System.out.println("tokenProvider Token : " + token);
                mDatabase.child(idUser).setValue(token);
            }
        });

        /*FirebaseInstallations.getInstance().getId().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                //devuelve el nuevo token de usuario
                Token token = new Token(s);                                         //posible error
                System.out.println("tokenProvider Token : " + token);
                mDatabase.child(idUser).setValue(token);
            }
        });*/
    }
    public DatabaseReference getToken(String idUser){
        return mDatabase.child(idUser);
    }
}
