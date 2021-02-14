package com.technologyg.taxiii.activities.client;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.includes.MyToolbar;
import com.technologyg.taxiii.providers.GoogleApiProvider;
import com.technologyg.taxiii.utils.DecodePoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DetailRequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap nMap;
    private SupportMapFragment nMapFragment;

    private double mExtraOrigLong;
    private double mExtraOrigLat;
    private double mExtraDesLong;
    private double mExtraDesLat;
    private String mExtraOrigin;
    private String mExtraDest;

    private LatLng mOriginLatLong;
    private LatLng mDestLatLong;

    private GoogleApiProvider mGoogleApiProvider;

    private List<LatLng> mPolylinesList;
    private PolylineOptions mPolylinesOption;

    private TextView mTxtOrigin;
    private TextView mTxtDestino;
    private TextView mTxtTime;
    private TextView mTxtDistancia;
    private Button mBtnSolicitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);

        MyToolbar.show(this, "Solicitar", false);

        //intancias
        nMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_client_detail);
        nMapFragment.getMapAsync(this);
        mGoogleApiProvider = new GoogleApiProvider(DetailRequestActivity.this);
        mTxtOrigin = findViewById(R.id.txtViewOrigin);
        mTxtDestino = findViewById(R.id.txtViewDest);
        mTxtTime = findViewById(R.id.txtViewTime);
        mTxtDistancia = findViewById(R.id.txtViewDistancia);
        mBtnSolicitar = findViewById(R.id.btn_Solicitar_driver);


        //Recibe los datos de la actividad anterior
        mExtraOrigLat = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOrigLong = getIntent().getDoubleExtra("origin_long", 0);
        mExtraDesLat = getIntent().getDoubleExtra("Dest_lat", 0);
        mExtraDesLong = getIntent().getDoubleExtra("Dest_long", 0);
        mOriginLatLong = new LatLng(mExtraOrigLat, mExtraOrigLong);
        mDestLatLong = new LatLng(mExtraDesLat, mExtraDesLong);
        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraDest = getIntent().getStringExtra("dest");

        mTxtOrigin.setText(mExtraOrigin);
        mTxtDestino.setText(mExtraDest);

        mBtnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRequestDriver();
            }
        });
   }

    private void goToRequestDriver() {
        Intent intent = new Intent(DetailRequestActivity.this, RequestDriverActivity.class);
        //latlng origin
        intent.putExtra("Olat", mOriginLatLong.latitude);
        intent.putExtra("Olng", mOriginLatLong.longitude);
        //origen - destino
        intent.putExtra("Origin", mExtraOrigin);
        intent.putExtra("destination", mExtraDest);
        //latlng destino
        intent.putExtra("DestLat",mDestLatLong.latitude);
        intent.putExtra("DestLng",mDestLatLong.longitude);

        startActivity(intent);
        finish();
    }

    private void drawRoute(){
        mGoogleApiProvider.getDirections(mOriginLatLong, mDestLatLong).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mPolylinesList = DecodePoints.decodePoly(points);
                    mPolylinesOption = new PolylineOptions();
                    mPolylinesOption.color(Color.BLUE);
                    mPolylinesOption.width(13f);
                    mPolylinesOption.startCap(new      SquareCap());
                    mPolylinesOption.jointType(JointType.ROUND);
                    mPolylinesOption.addAll(mPolylinesList);
                    nMap.addPolyline(mPolylinesOption);

                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");
                    mTxtTime.setText(durationText);
                    mTxtDistancia.setText(distanceText);

                }catch(Exception e){
                    System.out.println("Error en el método draw route: " + e);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        nMap.getUiSettings().setZoomControlsEnabled(true);

        //Añadir marcadores al mapa
        nMap.addMarker(new MarkerOptions().position(mOriginLatLong).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_start_user20)));
        nMap.addMarker(new MarkerOptions().position(mDestLatLong).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end_user20)));

        //mover la camara a otra posición
        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(mOriginLatLong)
                        .zoom(14f)
                        .build()));
        drawRoute();
    }
}