package com.technologyg.taxiii.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyg.taxiii.models.ClientBooking;

import java.util.HashMap;
import java.util.Map;

public class ClientBookingProviders {
    private DatabaseReference mDatabase;

    public ClientBookingProviders(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("clientBooking");
    }

    public Task<Void>create(ClientBooking clientBooking){
        System.out.println("ClientBookingProviders Create()");
        return mDatabase.child(clientBooking.getIdClient()).setValue(clientBooking);
    }

    public Task<Void> updateStatus(String idClientBooking, String status){
        System.out.println("ClientBookingProviders updateStatus()");
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        System.out.println("ClientBookingProviders: map:  " + map);
        return mDatabase.child(idClientBooking).updateChildren(map);
    }

    public DatabaseReference getStatus(String idClientBooking){
        System.out.println("ClientBookingProviders getStatus() idClientBooking: " + idClientBooking);
        return mDatabase.child(idClientBooking).child("status");
    }
}
