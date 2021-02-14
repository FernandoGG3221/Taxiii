package com.technologyg.taxiii.activities.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.activities.MainActivity;
import com.technologyg.taxiii.includes.MyToolbar;
import com.technologyg.taxiii.providers.AuthProvider;
import com.technologyg.taxiii.providers.ClientProvider;
import com.technologyg.taxiii.providers.GeofireProvider;
import com.technologyg.taxiii.providers.TokenProviders;

import java.util.Objects;

public class MapDriverBookingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AuthProvider mAhutProvider;
    private GoogleMap nMap;
    private SupportMapFragment nMapFragment;
    private GeofireProvider mGeofireProvier;
    private LatLng mCurrentLocation;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker nMarker;

    private TokenProviders mTokenProvider;
    private ClientProvider mClientProvider;

    private TextView mTextViewNameClientBooking;
    private TextView mTextViewEmailClientBooking;

    private String mExtraClientID;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    //Almacena la long y la lat
                    mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    if (nMarker != null){
                        nMarker.remove();
                    }
                    //mark icon
                    nMarker = nMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                            )
                                    .title("Taxiii posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.taxiii_azul100))
                    );
                    //location for client in real time
                    nMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    updateLocation();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver_booking);

        //Instancias
        mAhutProvider = new AuthProvider();
        mGeofireProvier = new GeofireProvider("drivers_working");
        System.out.println("MapDriverBookingActivity: onCreate(): mGeoFireProvider = " + mGeofireProvier);

        mTokenProvider = new TokenProviders();
        mClientProvider = new ClientProvider();
        nMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_driver_booking);

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        nMapFragment.getMapAsync(this);

        mTextViewNameClientBooking = findViewById(R.id.txtViewNameClientBooking);
        mTextViewEmailClientBooking = findViewById(R.id.txtViewEmailClientBooking);

        mExtraClientID = getIntent().getStringExtra("idClientBooking");
        System.out.println("MapDriverBookingActivity: iniciando ejecución del getClientBooking()" +
                "Linea 131 - 133");
        getClientBooking();

    }

    private void getClientBooking() {
        System.out.println("MDBA, getClientBooking();");
        mClientProvider.getClient(mExtraClientID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("MapDriverBooking getClient(): snapsot: "+snapshot);
                if(snapshot.exists()){
                    String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    String name = snapshot.child("name").getValue().toString();
                    String name1 = snapshot.child("apePat").getValue().toString();
                    String name2 = snapshot.child("apeMat").getValue().toString();
                    String nameComplet =  name +" "+ name1 + " " +name2;
                    System.out.println("Nombre completo: "+ nameComplet);
                    mTextViewNameClientBooking.setText(nameComplet);
                    mTextViewEmailClientBooking.setText(email);

                }else{
                    System.out.println("No existe la base de datos");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Linea 155 - 161" +
                        "MapDriverBookingActivity" +
                        "onCancelled()");
            }
        });

    }

    private void updateLocation(){
        if(mAhutProvider.existSession() && mCurrentLocation != null){
            mGeofireProvier.saveLocation(mAhutProvider.getId(), mCurrentLocation);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        nMap.getUiSettings().setZoomControlsEnabled(true);
        //location exactly
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        nMap.setMyLocationEnabled(false);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);

        starLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(GPSActivte()){
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }else{
                        ShowAlerDialogNoGPS();
                    }

                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && GPSActivte()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }else{
            ShowAlerDialogNoGPS();
        }
    }

    private void ShowAlerDialogNoGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS desactivado")
                .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();

    }

    private boolean GPSActivte(){
        boolean isActived = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActived = true;
        }
        return isActived;
    }

    private void disconnect(){
        if(mFusedLocation != null){
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if(mAhutProvider.existSession()){
                mGeofireProvier.remove(mAhutProvider.getId());
                Log.i("MapDriverBookingAct","disconnect(): Desconectado correctamente");
            }
        }else{
            Toast.makeText(this,"No puede desconcetarse aún",Toast.LENGTH_SHORT);
        }
    }

    private void starLocation(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(GPSActivte()){
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }else{
                    ShowAlerDialogNoGPS();
                }
            }else{
                checkLocationPermissions();
            }
        }else{
            if(GPSActivte()){
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }else{
                ShowAlerDialogNoGPS();
            }

        }
    }

    private void checkLocationPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Necesita persmisos para continuar")
                        .setMessage("Se requiere de permisos de ubicación para utilizar la aplicación")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Conceder permisos para acceder a la localización
                                ActivityCompat.requestPermissions(MapDriverBookingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(MapDriverBookingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
}