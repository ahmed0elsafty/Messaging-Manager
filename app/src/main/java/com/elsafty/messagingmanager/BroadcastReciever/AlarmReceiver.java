package com.elsafty.messagingmanager.BroadcastReciever;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.util.ArrayList;

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
        if (message.getGroupId().equals("0")){
            int contactId = mSqlCommunication.getContactIdByMessageID(SmsID);
            MyContact contact = mSqlCommunication.getContactById(contactId);
            sendMySMS(contact.getNumber(),message.getTxtMessage(),contact.getName());
        }else {
            ArrayList<MyContact> contacts = mSqlCommunication.getAllContactsInTheSameGroup(Integer.parseInt(message.getGroupId()));
            for (MyContact contact : contacts) {
                sendMySMS(contact.getNumber(), message.getTxtMessage(), contact.getName());
            }
        }
    }

    // Send sms
    public void sendMySMS(String phoneNumber, String message, String name) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            int subscriptionId = SmsManager.getDefaultSmsSubscriptionId();
            SmsManager MySmsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
            ArrayList<String> messages = MySmsManager.divideMessage(message);
            Intent intent = new Intent(getContext(), NotifyUser.class);

            intent.putExtra("name", name);
            intent.putExtra("SmsID", SmsID);

            ArrayList<PendingIntent> sentIntent = new ArrayList<>();
            sentIntent.add(PendingIntent.getBroadcast(getContext(), REQUEST_CODE_NOTIFY_USER, intent, 0));
            MySmsManager.sendMultipartTextMessage(phoneNumber,
                    null,
                    messages,
                    sentIntent,
                    null);

        }




        /*Hope it finds you helpful... I am using it in my projects works from on all devices above 4.0 to 8.0
int CurrentSmsParts = 0;

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
{

    int subscriptionId = SmsManager.getDefaultSmsSubscriptionId();
    SmsManager MySmsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
    ArrayList<String> messages = MySmsManager.divideMessage(message);

    MySmsManager.sendMultipartTextMessage(phonenumber, null,messages, null, null);
    CurrentSmsParts = messages.size();

  }
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
  {
       SmsManager MySmsManager = SmsManager.getDefault();
       ArrayList<String> messages =
       MySmsManager.divideMessage(message);

       MySmsManager.sendMultipartTextMessage(phonenumber, null,messages, null, null);
       CurrentSmsParts = messages.size();


    }*/
    }


}
