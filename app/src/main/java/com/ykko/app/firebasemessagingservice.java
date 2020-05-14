package com.ykko.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.messaging.RemoteMessage;
import com.ykko.app.ui.admin_home.AdminHomeActivity;
import com.ykko.app.ui.gallery.GalleryFragment;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class firebasemessagingservice extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    public firebasemessagingservice() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        Map data = remoteMessage.getData();
        String data_user = "NON";
        if(data.get("username") !=null){
           data_user = data.get("username").toString();
        }

        //String username = remoteMessage.getNotification().get();
        Log.d(TAG, "onMessageReceived: Message Received: \n" +
                "Title: " + title + "\n" +
                "Message: " + message);
        SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        //SharedPreferences.Editor edit = sp.edit();

        String username = sp.getString("key","NON");
//        if(data_user.equals(username)){
//            sendNotification(title,message);
//        }else if(data_user.equals("Admin")){
//            sendNotification(title,message);
//        }
        if(data_user.equals("Admin")){
            sendNotification(title,message,click_action);
        }else{
            if(data_user.equals(username)){
                sendNotification(title,message,click_action);
            }
        }
    }

    @Override
    public void onDeletedMessages() {

    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);




    }
    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void sendNotification(String title,String messageBody,String click_action) {
        Intent intent=new Intent();
        //Intent intent = new Intent(this, MainActivity.class);
        if(click_action.equals("CUSTOMER_HOME")){
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("GALLERY", "GALLERY" );

        }else if(click_action.equals("ADMIN_HOME")){
            intent = new Intent(this, AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }



        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }
    }

