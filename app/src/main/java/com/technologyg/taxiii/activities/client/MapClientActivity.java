package com.technologyg.taxiii.activities.client;

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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.SphericalUtil;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.activities.MainActivity;
import com.technologyg.taxiii.activities.driver.MapDriverActivity;
import com.technologyg.taxiii.includes.MyToolbar;
import com.technologyg.taxiii.providers.AuthProvider;
import com.technologyg.taxiii.providers.GeofireProvider;
import com.technologyg.taxiii.providers.TokenProviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapClientActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AuthProvider mAhutProvider;
    private GeofireProvider mGeofireProvier;
    private GoogleMap nMap;
    private SupportMapFragment nMapFragment;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker nMarker;
    private LatLng mCurrentLocation;
    private List<Marker> mDriverMarkets = new ArrayList<>();
    private boolean mIsFirstTime = true;

    private PlacesClient mPlaces;
    private AutocompleteSupportFragment mAutoComplete;
    private AutocompleteSupportFragment mAutoCompleteDest;

    private String mOrigin;
    private LatLng mOriginLatLng;

    private String mDest;
    private LatLng mDestLatLng;

    private GoogleMap.OnCameraIdleListener mCameraListener;

    private Button mBtn_requestDriver;

    private TokenProviders mTokenProvider;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    //Almacena la long y la lat
                    mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());


                   /*
                   if(nMarker != null){
                        nMarker.remove();
                    }
                    //mark icon
                   nMarker = nMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                            )
                                    .title("Taxiii posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_taxi_location))
                    );
                    */

                    //location for client in real time
                    nMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));

                    if (mIsFirstTime){
                        mIsFirstTime = false;
                        getActiveDriver();
                        limitSearch();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client);

        MyToolbar.show(this, "Mapa del Cliente", false);

        //Instancias
        mAhutProvider = new AuthProvider();
        mGeofireProvier = new GeofireProvider("active_drivers");
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mBtn_requestDriver = findViewById(R.id.btn_Solicitar);
        mTokenProvider = new TokenProviders();

        nMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_client);
        nMapFragment.getMapAsync(this);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }
        mPlaces = Places.createClient(this);
        instanceAutocompleteOrigin();
        instanceAutocompleteDest();
        onCameraMove();
        generateToken();

        mBtn_requestDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requstDriver();
            }
        });
    }

    private void requstDriver() {
        if(mOriginLatLng !=null && mDestLatLng!= null){
            Intent intent = new Intent(MapClientActivity.this, DetailRequestActivity.class);
            //pasar parematros de una actividad a otra
            intent.putExtra("origin_lat", mOriginLatLng.latitude);
            intent.putExtra("origin_long", mOriginLatLng.longitude);
            intent.putExtra("Dest_lat", mDestLatLng.latitude);
            intent.putExtra("Dest_long", mDestLatLng.longitude);
            intent.putExtra("origin", mOrigin);
            intent.putExtra("dest", mDest);


            startActivity(intent);
        }else{
            Toast.makeText(this,"Selecciona el origen y el destino por favor!", Toast.LENGTH_SHORT).show();
        }
    }

    private void limitSearch(){
        try{
            LatLng nort = SphericalUtil.computeOffset(mCurrentLocation, 80, 0);
            System.out.println("Método de LimitiSearch");
            System.out.println("Norte: " + nort);
            //LatLng northSide = SphericalUtil.computeOffset(mCurrentLocation, 5000, 0);
            LatLng southSide = SphericalUtil.computeOffset(mCurrentLocation, 80, 180);
            System.out.println("Sur: " + southSide);
            mAutoComplete.setCountry("MX");
            mAutoComplete.setLocationBias(RectangularBounds.newInstance(southSide, nort));
            mAutoCompleteDest.setCountry("MX");
            mAutoCompleteDest.setLocationBias(RectangularBounds.newInstance(southSide, nort));
        }catch (Exception e){
            System.out.println("Error: "+e);
        }
    }

    private void onCameraMove(){
        mCameraListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try{
                    Geocoder geocoder = new Geocoder(MapClientActivity.this);
                    mOriginLatLng = nMap.getCameraPosition().target;
                    List<Address> addressList = geocoder.getFromLocation(mOriginLatLng.latitude, mOriginLatLng.longitude, 1);
                    String city = addressList.get(0).getLocality();
                    String country = addressList.get(0).getCountryName();
                    String address = addressList.get(0).getAddressLine(0);
                    mOrigin = address + " " + city;
                    mAutoComplete.setText(address + " " + city);
                }catch(Exception e){
                    System.out.println("Error en el try: "+ e);
                }
            }
        };
    }

    private void instanceAutocompleteOrigin(){
        //Selección del origen
        mAutoComplete = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.placesAutocompleteOrigin);
        mAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutoComplete.setHint("Lugar de origen");
        mAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mOrigin = place.getName();
                mOriginLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }
    private void instanceAutocompleteDest(){
        //Selección del destino
        mAutoCompleteDest = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.placesAutocompleteDest);
        mAutoCompleteDest.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutoCompleteDest.setHint("Lugar de Destino");
        mAutoCompleteDest.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mDest = place.getName();
                mDestLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }
    private void getActiveDriver(){
        mGeofireProvier.getActiveDriver(mCurrentLocation, 1).addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override //Añade los marcadores de los conductores que se van conectando
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                for(Marker marker: mDriverMarkets){
                    if(marker.getTag() != null){
                        if(marker.getTag().equals(dataSnapshot)){
                            return; //no se añade el mismo marcador a la BD
                        }
                    }
                }
                //Almacena la posicion del conductor que se conecta
                LatLng driverLatLong = new LatLng(location.latitude, location.longitude);
                Marker marker = nMap.addMarker(new MarkerOptions().position(driverLatLong).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.taxiii_azul100)));
                //Se asigna al tag el id del conductor
                marker.setTag(dataSnapshot);
                //añadir el markador a la lista de marcadores
                mDriverMarkets.add(marker);
            }

            @Override //Elimina los marcadores de los conductores que se desconectan
            public void onDataExited(DataSnapshot dataSnapshot) {
                for(Marker marker: mDriverMarkets){
                    if(marker.getTag() != null){
                        if(marker.getTag().equals(dataSnapshot)){
                            marker.remove(); //Se elimina el markador
                            mDriverMarkets.remove(marker); //se elimina el markador de la lista de markadores
                            return;
                        }
                    }
                }
            }

            @Override //Actualiza la posicion del conductor o conductores
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
                for(Marker marker: mDriverMarkets){
                    if(marker.getTag() != null){
                        if(marker.getTag().equals(dataSnapshot)){
                            marker.setPosition(new LatLng(location.latitude, location.longitude));
                        }
                    }
                }
            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        nMap.getUiSettings().setZoomControlsEnabled(true);
        nMap.setOnCameraIdleListener(mCameraListener);
        //location exactly
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        nMap.setMyLocationEnabled(false);*/


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
                        nMap.setMyLocationEnabled(true);
                        //Se añade el codigo de "nMap.setMyLocationEnabled(true)" si se desea activar el icono azul
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
            //location exactly
            nMap.setMyLocationEnabled(true);

        }else if (requestCode == SETTINGS_REQUEST_CODE && !GPSActivte()){
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

    private void starLocation(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(GPSActivte()){
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
                    nMap.setMyLocationEnabled(true);
                    //Se añade el codigo de "nMap.setMyLocationEnabled(true)" si se desea activar el icono azul
                }else{
                    ShowAlerDialogNoGPS();
                }
            }else{
                checkLocationPermissions();
            }
        }else{
            if(GPSActivte()){
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
                nMap.setMyLocationEnabled(true);
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
                                ActivityCompat.requestPermissions(MapClientActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(MapClientActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout_C){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    void logout(){
        mAhutProvider.logout();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void generateToken(){
        mTokenProvider.create(mAhutProvider.getId());
    }
}