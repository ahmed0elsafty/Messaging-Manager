package com.elsafty.messagingmanager.Utilities;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import com.elsafty.messagingmanager.Activities.MainActivity;
import com.elsafty.messagingmanager.Activities.NewMessageActivity;
import com.elsafty.messagingmanager.R;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channeID";
    public static final String channelName = "channelName";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String notifyUser, String recepientName) {

        int appBlueColour = Color.parseColor("#039be5");

        PendingIntent mainActivityIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent smsManagerIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent smsScheduleIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NewMessageActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)


                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sms))
                .setSmallIcon(R.drawable.ic_sms)
                .setContentTitle("Scheduled SMS to " + recepientName)
                .setContentText(notifyUser)
                .setColor(appBlueColour)
                .setContentIntent(mainActivityIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "SMS Manager", smsManagerIntent)
                .addAction(R.mipmap.ic_launcher, "Schedule New SMS", smsScheduleIntent);
    }
}
