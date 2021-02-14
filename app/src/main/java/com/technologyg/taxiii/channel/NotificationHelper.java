package com.technologyg.taxiii.channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import com.technologyg.taxiii.R;

import java.net.URI;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import retrofit2.http.Url;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.technologyg.taxiii";
    private static final String CHANNEL_NAME = "Taxiii";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.O){
            channels();
        }else{
            System.out.println("Error al ejecutar el método \nNo cumple con la version mínima");
        }
    }

    //La creacion de un canal de notificaciones es obligatiora desde android oreo
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void channels(){
        System.out.println("NotHelper Ejecutando el método de channles()");
        NotificationChannel notificationChannel = new
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager(){
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        System.out.println("NotifHelper: getManager() manager" + manager);
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String body, PendingIntent intent, Uri soundUri){
        System.out.println("SDK Oreo");
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));

    }

    public NotificationCompat.Builder getNotificationCompact(String title, String body, PendingIntent intent, Uri soundUri){
        System.out.println("SDK < Oreo");
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotificationActions(String title, String body, Uri soundUri, Notification.Action acceptAction, Notification.Action cancelAction){
        System.out.println("SDK Oreo");
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_stat_name)
                .addAction(acceptAction)
                .addAction(cancelAction)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));

    }

    public NotificationCompat.Builder getNotificationCompactAction(String title, String body, Uri soundUri, NotificationCompat.Action acceptAction, NotificationCompat.Action cancelAction){
        System.out.println("SDK < Oreo");
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_stat_name)
                .addAction(acceptAction)
                .addAction(cancelAction)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

}
