package mx.gob.jalisco.edu.consultaescolar.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import mx.gob.jalisco.edu.consultaescolar.MainActivity;
import mx.gob.jalisco.edu.consultaescolar.R;


public class NotificationsService extends FirebaseMessagingService {
    private static final String TAG = NotificationsService.class.getSimpleName();
    RemoteMessage message;
    @Override public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        message = remoteMessage;

        //Objects.equals(remoteMessage.getFrom(), "/topics/news")

        sendSimpleNotification(remoteMessage.getNotification(), remoteMessage.getData());

    }

    private void sendSimpleNotification(RemoteMessage.Notification notification, Map< String, String > data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = Color.parseColor("#3C9B95");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setContentInfo("")
                .setTicker("")
                .setAutoCancel(true)
                .setColor(color)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_action_social_notifications : R.drawable.ic_action_social_notifications;
    }
}