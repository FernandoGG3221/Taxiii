package com.technologyg.taxiii.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyg.taxiii.models.Driver;

public class DriverProvider {
    DatabaseReference mDatabase;
    public DriverProvider(){
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        System.out.println("mDataBaseDriver: " + mDatabase.toString());
    }
    public Task<Void> create(Driver driver){
        return mDatabase.child(driver.getId()).setValue(driver);
    }
}
