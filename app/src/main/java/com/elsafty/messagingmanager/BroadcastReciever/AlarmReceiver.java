package com.elsafty.messagingmanager.BroadcastReciever;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    public Context context;
    private SqlCommunication mSqlCommunication;
    private int SmsID;

    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);
        // initialise database
        mSqlCommunication = new SqlCommunication(context);

        // Receive SmsID from alarm extra
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SmsID = bundle.getInt("SmsID");
        }
        getSmsDetails();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // Retrieve sms details from database using SmsID from alarm
    public void getSmsDetails() {
        MyMessage message= mSqlCommunication.getMessageById(SmsID);
        String number = message.getNumber();
        String txtmessage = message.getTxtMessage();
        String name = message.getName();
        sendMySMS(number, txtmessage, name);
    }

    // Send sms
    public void sendMySMS(String phoneNumber, String message, String name) {
        SmsManager sms = SmsManager.getDefault();
        // If message is too long for single sms split into multiple messages
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            Intent intent = new Intent(getContext(), NotifyUser.class);

            // Pass recipient name and SmsID
            intent.putExtra("name", name);
            intent.putExtra("SmsID", SmsID);

            // Broadcast receiver to listen for the sentstatus of the message.
            PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(), SmsID, intent, 0);
            sms.sendTextMessage(phoneNumber, null, msg, sentIntent, null);
        }
    }
}
