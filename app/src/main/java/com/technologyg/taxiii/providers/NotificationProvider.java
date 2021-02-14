package com.technologyg.taxiii.providers;

import com.technologyg.taxiii.models.FCMBody;
import com.technologyg.taxiii.models.FCMResponse;
import com.technologyg.taxiii.retrofit.IFCMApi;
import com.technologyg.taxiii.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call<FCMResponse>sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}
