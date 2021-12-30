package com.technologyg.taxiiiclient;

import androidx.activity.result.ActivityResult;
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
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.technologyg.taxiiiclient.includes.MyToolbar;
import com.technologyg.taxiiiclient.providers.AuthProvider;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //PROVIDERS
    private AuthProvider mAuthProvider;
    //MAPS
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    //LOCATIONS
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    //VARIABLES
    private final int LOCATION_REQUEST_CODE = 1;        //bandera para permitirle saber si se necesitan pedir los permisos de ubicacion
    private final int SETTINGS_REQUEST_CODE = 2;

    //Escucha cada vez que el usuario se mueve
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    //Obtener la localización del usuario en tiempo real
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15f).build()));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //INSTANCES
        mAuthProvider = new AuthProvider();
        //  Iniciar o detener la ubicación del cliente
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        //Find By Id
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_MapActivity);
        //Sync
        mMapFragment.getMapAsync(MapActivity.this);

        //Toolbar
        MyToolbar.show(MapActivity.this, "¡Taxiii!");

        //Methods
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(1);
        //Location exactly for the user
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
        mMap.setMyLocationEnabled(true);
        //INSTANCE
        mLocationRequest = new LocationRequest();
        //Tiempo que se va a actualizando la ubicacion del usuario en el mapa
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        //Hace uso del gps con la mayor precision posible   //LocationRequest.PRIORITY_HIGH_ACCURACY == 100
        mLocationRequest.setPriority(100);
        //desplazamiento minimo entre ubicacion actualizada en metros
        mLocationRequest.setSmallestDisplacement(5);

        //Methods
        starLocation();
    }

    //Know if gps is enabled
    private boolean gpsActived(){
        boolean isActived = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //isProvider enable == si tiene el gps activado
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActived = true;
        }
        return isActived;
    }

    //Show Alert Dialog for go to settings and turn on gps
    private void showAlertDialogNoGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setMessage("Activa tu GPS para disfrutar de ¡Taxiii!").setPositiveButton("Configuración", (dialog, which) -> {
            //Es una actividad que cuando se inicia, espera que el usuario encienda su gps
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_REQUEST_CODE && gpsActived()){
            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }else{
            showAlertDialogNoGPS();
        }
    }

    //GET PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST_CODE){
            //pregunta si el usuario concedió los permisos
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    if(gpsActived()){
                        //Cuando este evento se ejecuta, entra al LocationCallBack y obtiene la ubicacion del usuario en tiepo real
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }else{
                        showAlertDialogNoGPS();
                    }
                }else{
                    checkLocationPermission();
                }
            }else{
                checkLocationPermission();
            }
        }
    }

    //Iniciar el escuchador de la ubicacion
    private void starLocation(){
        //Comprobar la version del sdk de android sea mayor a Marshmellow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if (gpsActived()){
                    //Cuando este evento se ejecuta, entra al LocationCallBack y obtiene la ubicacion del usuario en tiepo real
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }else {
                    showAlertDialogNoGPS();
                }
            }else{
                checkLocationPermission();
            }
        }
        else{
            if(gpsActived()){
                //Cuando este evento se ejecuta, entra al LocationCallBack y obtiene la ubicacion del usuario en tiepo real
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }else{
                showAlertDialogNoGPS();
            }
        }
    }

    //Verificar si el usuario acepta o no los permisos del gps
    private void checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(MapActivity.this)
                    .setTitle("Se requiere el permiso para continuar")
                    .setMessage("¡Taxiii! requiere del uso de GPS para brindarte el servicio")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        //Conceder permisos para acceder a la localización del celular
                        ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    }).create().show();
            }else{
                //Conceder permisos para acceder a la localización del celular
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    //Create option to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Declarar el menú a utilizar
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Select item of menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        mAuthProvider.logout();
        Intent i = new Intent(MapActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}