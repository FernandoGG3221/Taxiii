package com.technologyg.taxiii.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.models.ClientBooking;
import com.technologyg.taxiii.models.FCMBody;
import com.technologyg.taxiii.models.FCMResponse;
import com.technologyg.taxiii.providers.AuthProvider;
import com.technologyg.taxiii.providers.ClientBookingProviders;
import com.technologyg.taxiii.providers.GeofireProvider;
import com.technologyg.taxiii.providers.GoogleApiProvider;
import com.technologyg.taxiii.providers.NotificationProvider;
import com.technologyg.taxiii.providers.TokenProviders;
import com.technologyg.taxiii.utils.DecodePoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class RequestDriverActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextView;
    private Button mBtn_Cancel;

    private GeofireProvider mGeofireProvider;
    private String mExtraOrigin;
    private String mExtraDest;
    private double mExtraLat;
    private double mExtraLng;
    private double mExtraDestLat;
    private double mExtraDestLng;
    private LatLng mOriginLL;
    private LatLng mDestLL;

    private double mRadius = 0.1;
    private boolean mDriverFound = false;
    private String mIDDriverFound = "";
    private LatLng mDriverFoundLatLng;

    private NotificationProvider mNotificationProvider;
    private TokenProviders mTokenProvider;
    private ClientBookingProviders mBookingProvider;
    private AuthProvider mAutProvider;
    private GoogleApiProvider mGoogleApiProvider;
    //variable que permite que el eventListener deje de escuchar la bd
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_driver);

        //instances by ID
        mAnimation = findViewById(R.id.animation);
        mTextView = findViewById(R.id.txtViewSearchTaxiii);
        mBtn_Cancel = findViewById(R.id.btn_cancel_request);

        mAnimation.playAnimation();
        //paso de param de origin
        mExtraOrigin = getIntent().getStringExtra("Origin");
        //paso de param de destination
        mExtraDest = getIntent().getStringExtra("destination");
        //paso de param de latlng origin
        mExtraLat = getIntent().getDoubleExtra("Olat", 0);
        mExtraLng = getIntent().getDoubleExtra("Olng", 0);
        //paso de param de latlng dest
        mExtraDestLat = getIntent().getDoubleExtra("DestLat", 0);
        mExtraDestLng= getIntent().getDoubleExtra("DestLng",0);
        //almacena latlng origin
        mOriginLL = new LatLng(mExtraLat, mExtraLng);
        //almacena latlng dest
        mDestLL = new LatLng(mExtraDestLat, mExtraDestLng);

        //Instance by objects
        mNotificationProvider = new NotificationProvider();
        mGeofireProvider = new GeofireProvider("active_drivers");
        mTokenProvider = new TokenProviders();
        mBookingProvider = new ClientBookingProviders();
        mAutProvider = new AuthProvider();
        mGoogleApiProvider = new GoogleApiProvider(RequestDriverActivity.this);

        getClosesDrivers();
    }

    private void getClosesDrivers(){ //Búsqueda del conductor más cercano
        mGeofireProvider.getActiveDriver(mOriginLL, mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mDriverFound){
                    mDriverFound = true;
                    mIDDriverFound = key;
                    System.out.println("RequestDriverAct mIdDriver: "+ mIDDriverFound );
                    mDriverFoundLatLng = new LatLng(location.latitude, location.longitude);
                    mTextView.setText("Esperando respuesta de conductores");
                    createClientBooking();
                    Log.i("RequestDriverA","ID del conductor" + mIDDriverFound);

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //Ingresa cuando el método de búsqueda termina
                if(!mDriverFound){
                    mRadius = mRadius + 0.1f;
                    System.out.println("RequestDriverAc Radio = " + mRadius);
                    if(mRadius > 5){
                        Toast.makeText(RequestDriverActivity.this, "No hay conductores cerca disponibles!", Toast.LENGTH_SHORT);
                        mTextView.setText("No se encontraron conductores disponibles :C");
                        return;
                    }
                    else{
                        getClosesDrivers();
                    }
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    public void createClientBooking(){
        mGoogleApiProvider.getDirections(mOriginLL, mDriverFoundLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");
                    //Se ejecuta el método de envio de notificacion
                    sendNotification(durationText, distanceText);

                }catch(Exception e){
                    System.out.println("Error en el método draw route: " + e);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void sendNotification(final String time, final String distance){
        System.out.println("RequestDriverActivity: Entrando al método de notificación" );
        mTokenProvider.getToken(mIDDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Log.i("RequestDriverAc", "mTokenProvider"+ mTokenProvider);
                    Log.i("RequestDriverA"," snapsot: " + snapshot);

                    String token = snapshot.child("token").getValue().toString();

                    Log.i("RequestDriverAc"," token: " + token);

                    Map<String, String> map = new HashMap<>();

                    map.put("title", "SOLICITUD DE SERVICIO A "+ time + " DE TU POSICIÓN");
                    map.put("body", "Un cliente solicita un servicio a una distancia de " + distance + "\n" +
                            "Recoger en: " + mExtraOrigin+"\n"+
                            "Destino: " + mExtraDest);
                    map.put("idClient", mAutProvider.getId());

                    FCMBody fcmBody = new FCMBody(token, "high", map);
                    Log.i("RequestDriverAct", " fcmBody: " + fcmBody);

                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            Log.i("ReDrAc","Entrando al metodo de onResponse \nlínea 214");
                            Log.i("ReDrAc","Response: "+response);
                            if(response.body() != null){
                                Log.i("ReDrAc","El body no está vacio");
                                Log.i("ReDrAc","response -->"+response);
                                Log.i("ReDrAc","Response body: "+response.body());
                                Log.i("ReDrAc","Response Body getSuccess: "+response.body().getSuccess());
                                try{
                                    Log.i("ReDrAc","Dentro del try");
                                    if(response.body().getSuccess() == 1){

                                        Log.i("ReDrAc","Notificación enviada");
                                        mTextView.setText("ESPERANDO RESPUESTA DEL CONDUCTOR");
                                        Toast.makeText(RequestDriverActivity.this, "Notificación enviada", Toast.LENGTH_SHORT).show();

                                        //Creación de el bookingClient
                                        ClientBooking clientBooking = new ClientBooking(
                                                mIDDriverFound,
                                                mAutProvider.getId(),
                                                mExtraOrigin,
                                                mExtraDest,
                                                time,
                                                distance,
                                                "create",
                                                mExtraLat,
                                                mExtraLng,
                                                mExtraDestLat,
                                                mExtraDestLng
                                        );

                                        mBookingProvider.create(clientBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("RequestDriver","create client booking, onSuccess");
                                                checkStatusClientBooking();
                                                Toast.makeText(RequestDriverActivity.this, "Client Book Se creó", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else if(response.body().getSuccess() == 0) {
                                        Log.i("ReqDrivAct","fallo de envio de notificacion");
                                        Toast.makeText(RequestDriverActivity.this, "No se pudo enviar la notificación", Toast.LENGTH_SHORT).show();
                                        mTextView.setText(":´C");
                                    }

                                }catch(Exception e){
                                    Log.i("RequestDrivA",": Error en el try Not: " + e);
                                }
                            }else{
                                Log.i("reqDrivAct"," Error en la comprobacion");
                            }

                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.i("RequestDriverAc","Error onFailure: " + t.getMessage());
                            Toast.makeText(RequestDriverActivity.this, "Se produjo un error", Toast.LENGTH_SHORT).show();
                            mTextView.setText("Intentar de nuevo");
                        }
                    });
                }else{
                    Log.i("RequestDriverActivity","Error: El conductor no tiene token de sesión");
                    Toast.makeText(RequestDriverActivity.this, "Se produjo un error, el conductor no tiene un token de sesion", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Escucha los datos de firebase en tiempo real
    private void checkStatusClientBooking() {
        System.out.println("RequestDriverAct: checkStatusClientBooking() iniciando");
        mListener = mBookingProvider.getStatus(mAutProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override //addValueEventListener es un escuchador que se mantiene constante hasta que no se finalice de alguna manera
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("RequestDriverAct: onDataChange()");
                System.out.println("RequestDriverAct: Snapshot : " + snapshot);
                if(snapshot.exists()){
                    String status = snapshot.getValue().toString();
                    System.out.println("Status: " + status);
                    if(status.equals("accept")){
                        Toast.makeText(RequestDriverActivity.this, "El conductor aceptó el servicio", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RequestDriverActivity.this, MapClientBookingActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("cancel")){
                        Toast.makeText(RequestDriverActivity.this, "El conductor no aceptó el servicio", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RequestDriverActivity.this, MapClientActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("RequestDriverActivity:  onCancelled() \nError");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListener != null){
            mBookingProvider.getStatus(mAutProvider.getId()).removeEventListener(mListener);
        }

    }
}