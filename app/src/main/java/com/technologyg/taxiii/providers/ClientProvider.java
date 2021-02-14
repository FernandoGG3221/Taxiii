package com.technologyg.taxiii.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyg.taxiii.models.Client;

public class ClientProvider {
    DatabaseReference mDatabase;
    public ClientProvider(){
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Clients");
        System.out.println("ClientProvider, mDataBaseClient: " + mDatabase.toString());
    }
    public Task<Void> create(Client client){
        return mDatabase.child(client.getId()).setValue(client);
    }

    public DatabaseReference getClient(String idClient) {
        System.out.println("ClientProvider, idClient " + idClient);
        return mDatabase.child(idClient);
    }
}
