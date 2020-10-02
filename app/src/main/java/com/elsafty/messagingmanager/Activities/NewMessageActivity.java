package com.elsafty.messagingmanager.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elsafty.messagingmanager.BroadcastReciever.NotifyUser;
import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SmsContract;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.SEND_SMS;
import static java.lang.Boolean.TRUE;

public class NewMessageActivity extends AppCompatActivity {
    private static final int REQUEST_SMS = 555;
    private static final int REQUEST_CODE_NOTIFY_USER = 444;
    private ImageButton btnSend;
    private EditText editTxtMessage;
    private TextView txtName;
    private TextView txtNumber;
    private String name, number, message;
    private SqlCommunication sqlCommunication;
    private int contactID;
    private int SmsID;

    private static boolean AirplaneModeOn(Context context) {
        // Return true if airplane more is on
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        btnSend = findViewById(R.id.btn_send_newmessage);
        editTxtMessage = findViewById(R.id.edit_newMessage);
        txtName = findViewById(R.id.txtMessageName);
        txtNumber = findViewById(R.id.txtMessageNumber);
        sqlCommunication = new SqlCommunication(this);
        Intent intent = getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");
        name = bundle.getString("name");
        number = bundle.getString("number");
        contactID = bundle.getInt("id");
        txtName.setText(name);
        txtNumber.setText(number);
        message = editTxtMessage.getText().toString();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int SMSPermission = checkSelfPermission(SEND_SMS);
                    if (SMSPermission != PackageManager.PERMISSION_GRANTED) {
                        requestSmsPermission();
                        return;
                    }
                    validateInput();
                }
            }
        });
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_SMS);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_SMS) {// If result is permission granted attempt to schedule sms
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                validateInput();
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Display message informing user they must allow permission, if user selects ok ask again
                    if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                        showMessageOKCancel("You must allow this permission to schedule an SMS",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestSmsPermission();
                                        }
                                    }
                                });
                        return;
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void validateInput() {
        message = editTxtMessage.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a message.", Toast.LENGTH_SHORT).show();
            return;
        } else if (AirplaneModeOn(getApplicationContext()) == TRUE) {
            Toast.makeText(getApplicationContext(), "SMS will not send in airplane mode. Please select an option:", Toast.LENGTH_SHORT).show();
            return;
        }else {
            createMessage(name,number,message);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(NewMessageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void resetInput() {
        editTxtMessage.setText("");
        txtNumber.setText("");
        txtName.setText("");
        name = "";
        number = "";
    }
    private void createMessage(String contactName, String phoneNumber, String messageText) {
        Date date = new Date();
        String name = contactName;
        String number = phoneNumber;
        String message = messageText;
        String messageDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            messageDate = java.time.LocalDate.now().toString();
        }
        String messageTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            messageTime = java.time.LocalTime.now().toString();
        }
        String messageStatus =String.valueOf(SmsContract.STATUS_SENT);

        // Start multi-thread to insert sms to database and start alarm manager
        ScheduleSmsAsyncTask task = new ScheduleSmsAsyncTask();
        task.execute("0", message, messageDate, messageTime, messageStatus);
    }
    private class ScheduleSmsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... string) {

            try {
                // Construct a Sms object and pass it to the helper for database insertion
                SmsID = sqlCommunication.insertMessage(new MyMessage(string[0], string[1], string[2], string[3], string[4]),
                        contactID);
                sendMySMS(number,message,name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resetInput();
        }
    }
    public void sendMySMS(String phoneNumber, String message, String name) {
        SmsManager sms = SmsManager.getDefault();
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            Intent intent = new Intent(this, NotifyUser.class);

            intent.putExtra("name", "");
            intent.putExtra("SmsID", SmsID);

            PendingIntent sentIntent = PendingIntent.getBroadcast(this, REQUEST_CODE_NOTIFY_USER, intent, 0);
            sms.sendTextMessage(phoneNumber, null, msg, sentIntent, null);
        }
    }
}