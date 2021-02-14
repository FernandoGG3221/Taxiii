package com.technologyg.taxiii.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.technologyg.taxiii.activities.client.MapClientBookingActivity;
import com.technologyg.taxiii.providers.AuthProvider;
import com.technologyg.taxiii.providers.ClientBookingProviders;
import com.technologyg.taxiii.providers.GeofireProvider;

public class AcceptReceiver extends BroadcastReceiver{
    private ClientBookingProviders mclientBookingProviders;
    private GeofireProvider mGeofireProvider;
    private AuthProvider mAuthProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        //instances
        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider("active_drivers");
        mGeofireProvider.remove(mAuthProvider.getId());

        String idClient = intent.getExtras().getString("idClient");
        System.out.println("AcceptReceiver idClient: " + idClient);

        mclientBookingProviders = new ClientBookingProviders();
        mclientBookingProviders.updateStatus(idClient, "accept");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, MapClientBookingActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        //Envio de parámetros
        intent1.putExtra("idClientBooking", idClient);
        context.startActivity(intent1);

    }
}
