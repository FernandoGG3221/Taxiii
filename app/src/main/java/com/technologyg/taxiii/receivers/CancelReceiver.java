package com.technologyg.taxiii.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.technologyg.taxiii.providers.ClientBookingProviders;

public class CancelReceiver extends BroadcastReceiver {
    private ClientBookingProviders mclientBookingProviders;

    @Override
    public void onReceive(Context context, Intent intent) {
        String idClient = intent.getExtras().getString("idClient");
        System.out.println("CancelReceiver idClient: " + idClient);

        mclientBookingProviders = new ClientBookingProviders();
        mclientBookingProviders.updateStatus(idClient, "cancel");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }
}
