package com.technologyg.taxiii.providers;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseDriverWorking;
    private GeoFire mGeofire;

    public GeofireProvider(String reference){
        mDatabase = FirebaseDatabase.getInstance().getReference().child(reference);
        mGeofire = new GeoFire(mDatabase);
    }

    public void saveLocation(String idDriver, LatLng latLng){
        mGeofire.setLocation(idDriver, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void remove(String idDriver){
        mGeofire.removeLocation(idDriver);
    }

    public GeoQuery getActiveDriver(LatLng latLng,  double radius){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

    public DatabaseReference isDriverWorking(String idDriver){
        Log.i("GeoFireProvider", "isDriverWorking(): Creando el nodo de 'drivers-working'");
        System.out.println("GeoFireProvider: DatabaseReference " + idDriver);

        try{
            System.out.println("dentro del try");
            mDatabaseDriverWorking =  FirebaseDatabase.getInstance().getReference().child("drivers_working").child(idDriver);
            System.out.println(mDatabaseDriverWorking);
        }catch (Error e){
            System.out.println("GeofireProvider: error en el try");
            System.out.println("error: " + e);
        }
        return mDatabaseDriverWorking;
    }
}
