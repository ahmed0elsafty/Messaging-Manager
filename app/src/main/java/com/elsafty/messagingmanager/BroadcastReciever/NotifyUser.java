package com.elsafty.messagingmanager.BroadcastReciever;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.elsafty.messagingmanager.SQLite.SmsContract;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;
import com.elsafty.messagingmanager.Utilities.NotificationHelper;

import androidx.core.app.NotificationCompat;

public class NotifyUser extends BroadcastReceiver {

    public Context context;
    String recepientName;
    int SmsID;
    private SqlCommunication mSqlCommunication;

    @Override
    public void onReceive(Context context, Intent intent) {
        mSqlCommunication = new SqlCommunication(context);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            recepientName = bundle.getString("name");
            SmsID = bundle.getInt("SmsID");
        }

        String messageSentStatus = "Message could not be sent.\nUnknown Error";
        setContext(context);
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                messageSentStatus = "Message has been sent.";
                updateSmsToSent();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                messageSentStatus = "Message could not be sent.\nGeneric Failure Error";
                updateSmsToFailed();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                messageSentStatus = "Message could not be sent.\nNo Service Available";
                updateSmsToFailed();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                messageSentStatus = "Message could not be sent.\nNull PDU";
                updateSmsToFailed();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                messageSentStatus = "Message could not be sent.\nRadio is off";
                updateSmsToFailed();
                break;
            default:
                break;
        }
            if (!recepientName.isEmpty()){
                sendNotification(messageSentStatus);
            }

    }

    public void updateSmsToSent() {
        mSqlCommunication.updateMessage(SmsID, SmsContract.STATUS_SENT);
    }

    public void updateSmsToFailed() {
        mSqlCommunication.updateMessage(SmsID,SmsContract.STATUS_FAILED);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void sendNotification(String notificationMessage) {
        NotificationHelper notificationHelper = new NotificationHelper(getContext());
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notificationMessage, recepientName);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
