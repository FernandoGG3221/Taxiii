package com.technologyg.taxiii.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technologyg.taxiii.R;
import com.technologyg.taxiii.channel.NotificationHelper;
import com.technologyg.taxiii.receivers.AcceptReceiver;
import com.technologyg.taxiii.receivers.CancelReceiver;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingClient  extends FirebaseMessagingService {

    private static final int NOTIFICATION_CODE = 100;

    //GENERA UN TOKEN QUE PERMITE ENVIAR LAS NOTIFICACIONES A TRAVES DE FIREBASE CLOUD MESSAGING
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s); //genera un token de usuario con el fin de enviar notificaciones de dispositivo a dispositivo
    }

    @Override //Método donde se estarán recibiendo las notificaciones push que lleguen desde el servidor
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println("MyFireBaseMessagingClient onMessageReceived() iniciando...");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        System.out.println("MyFireBaseMessagingClient notification: " + notification);
        Map<String, String> data = remoteMessage.getData();
        System.out.println("MyFireBaseMessagingClient map<data>: " + data);
        String title = data.get("title");
        String body = data.get("body");

        System.out.println("getTitle: "+ title);
        System.out.println("getBody: "+ body);

        if(title != null){
            System.out.println("Probando configuracion: id o idClient");

            String id = data.get("idClient");               //id    |   idClient
            System.out.println("id del cliente: " + id);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                System.out.println("MyFirebaseMessagingClient Version de sdk api >= O");

                if(title.contains("SOLICITUD DE SERVICIO")){
                    System.out.println("NotificationApiOreoAction");

                    showNotificationsApiOreoAction(title, body, id);
                }else{
                    System.out.println("NotificationApiOreo whit action");

                    showNotificationsApiOreo(title, body);
                }
            }else{
                System.out.println("MyFirebaseMessagingClient Version de sdk api < O");
                if(title.contains("SOLICITUD DE SERVICIO")) {
                    System.out.println("NotificationAction");

                    showNotificationsAction(title,body,id);
                }else{
                    System.out.println("Notification sin action");

                    showNotification(title,body);
                }

            }
        }else{
            System.out.println("El tituto está vacio en la clase de My firebase Messaging Client");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationsApiOreo(String title, String body) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotification(title, body, intent, sound);
        notificationHelper.getManager().notify(1, builder.build());
    }

    private void showNotification(String title, String body){
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationCompact(title, body, intent, sound);
        notificationHelper.getManager().notify(1, builder.build());
    }

    //Versiones mayores  o iguales a Oreo
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationsApiOreoAction(String title, String body, String idclient) {
        //opcion de aceptar en la notificacion
        Intent acceptIntent = new Intent(this, AcceptReceiver.class);
        acceptIntent.putExtra("idClient", idclient);
        System.out.println("MyFirebaseMessagingClient idClient: " + idclient);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Action acceptAction = new Notification.Action.Builder(
                R.mipmap.ic_launcher,
                "Aceptar",
                acceptPendingIntent
        ).build();

        //opcion de cancelar en la notificacion
        Intent cancelIntent = new Intent(this, CancelReceiver.class);
        cancelIntent.putExtra("idClient", idclient);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Action cancelAction = new Notification.Action.Builder(
                R.mipmap.ic_launcher,
                "Cancelar",
                cancelPendingIntent
        ).build();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotificationActions(title, body, sound, acceptAction, cancelAction);
        notificationHelper.getManager().notify(2, builder.build());
    }

    //Versiones menores a Oreo
    private void showNotificationsAction(String title, String body, String idclient) {
        //opcion de aceptar en la notificacion
        Intent acceptIntent = new Intent(this, AcceptReceiver.class);
        acceptIntent.putExtra("idClient", idclient);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action acceptAction = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Aceptar",
                acceptPendingIntent
        ).build();

        //opcion de cancelar en la notificacion
        Intent cancelIntent = new Intent(this, CancelReceiver.class);
        cancelIntent.putExtra("idClient", idclient);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_CODE, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action cancelAction = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Cancelar",
                cancelPendingIntent
        ).build();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationCompactAction(title, body, sound, acceptAction,cancelAction);
        notificationHelper.getManager().notify(2, builder.build());
    }
}
