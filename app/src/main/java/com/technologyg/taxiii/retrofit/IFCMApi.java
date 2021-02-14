package com.technologyg.taxiii.retrofit;

import com.technologyg.taxiii.models.FCMBody;
import com.technologyg.taxiii.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//IFCMApi: Interface Firebase Cloud Messaging Api
public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAi5Joo5E:APA91bGLUmniF4o5G_Wr_rzOppw5rPYd75u9LymEcu-01csrUPL2adhxg0hl_DZizaR1n3D1Zw_skAAfPV6BxVs3ZilbdgCcZ0wtno_ALamQF6aHUT6nQrrnxqhYPSt3KqoqMotvsy09"
    })

    //Realizar peticion a "fcm/send" que es  la ruta que permitirá enviar notificaciones
    @POST("fcm/send")
        //Recibe un modelo
    Call<FCMResponse> send(@Body FCMBody body);
}
