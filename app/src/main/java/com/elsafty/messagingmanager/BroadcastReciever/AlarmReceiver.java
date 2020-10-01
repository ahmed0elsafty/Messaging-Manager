package com.elsafty.messagingmanager.BroadcastReciever;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int REQUEST_CODE_NOTIFY_USER = 888;
    public Context context;
    private SqlCommunication mSqlCommunication;
    private int SmsID;

    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);
        mSqlCommunication = new SqlCommunication(context);

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

    public void getSmsDetails() {
        MyMessage message= mSqlCommunication.getMessageById(SmsID);
        ArrayList<MyContact> contacts = mSqlCommunication.getAllContactsInTheSameGroup(Integer.parseInt(message.getGroupId()));
        for (MyContact contact:contacts) {
            sendMySMS(contact.getNumber(), message.getTxtMessage(), contact.getName());
        }
    }

    // Send sms
    public void sendMySMS(String phoneNumber, String message, String name) {
        SmsManager sms = SmsManager.getDefault();
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            Intent intent = new Intent(getContext(), NotifyUser.class);

            intent.putExtra("name", name);
            intent.putExtra("SmsID", SmsID);

            PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(), REQUEST_CODE_NOTIFY_USER, intent, 0);
            sms.sendTextMessage(phoneNumber, null, msg, sentIntent, null);
        }
    }
}
