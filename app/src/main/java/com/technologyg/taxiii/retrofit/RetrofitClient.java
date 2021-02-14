package com.technologyg.taxiii.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    public static Retrofit getClient(String url){
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
        System.out.println("RetrofitClient retrofitClient() retrofit: " + retrofit);
        return retrofit;
    }
    //método que permite enviar una peticion http a un
    //servicio de firebase para enviar las notificaciones de dispositivo a dispositivo
    public static Retrofit getClientObject(String url){
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        System.out.println("RetrofitClient retrofitobject() retrofit: " + retrofit);
        return retrofit;
    }
}
