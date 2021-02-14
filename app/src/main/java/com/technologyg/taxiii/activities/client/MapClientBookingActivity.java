package com.technologyg.taxiii.activities.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.providers.AuthProvider;
import com.technologyg.taxiii.providers.GeofireProvider;
import com.technologyg.taxiii.providers.TokenProviders;

import java.util.ArrayList;
import java.util.List;

public class MapClientBookingActivity extends AppCompatActivity implements OnMapReadyCallback{

    private AuthProvider mAhutProvider;
    private GeofireProvider mGeofireProvier;
    private GoogleMap nMap;
    private SupportMapFragment nMapFragment;


    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker nMarker;
    private LatLng mCurrentLocation;
    private List<Marker> mDriverMarkets = new ArrayList<>();
    private boolean mIsFirstTime = true;

    private PlacesClient mPlaces;

    private String mOrigin;
    private LatLng mOriginLatLng;

    private String mDest;
    private LatLng mDestLatLng;

    private TokenProviders mTokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_booking);

        //Instancias
        mAhutProvider = new AuthProvider();
        mGeofireProvier = new GeofireProvider("drivers_working");
        mTokenProvider = new TokenProviders();

        nMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_client_booking);
        nMapFragment.getMapAsync(this);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }
        mPlaces = Places.createClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        nMap.getUiSettings().setZoomControlsEnabled(true);

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

    }
}