package com.elsafty.messagingmanager.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elsafty.messagingmanager.BroadcastReciever.AlarmReceiver;
import com.elsafty.messagingmanager.BroadcastReciever.NotifyUser;
import com.elsafty.messagingmanager.Fragments.DateFragment;
import com.elsafty.messagingmanager.Fragments.TimeFragment;
import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.Pojos.MyDate;
import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.Pojos.MyTime;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SmsContract;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import static android.Manifest.permission.SEND_SMS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ScheduleMessageActivity extends AppCompatActivity {
    private static final int REQUEST_SMS = 1;
    private static final int REQUEST_CODE_NOTIFY_USER = 888;
    private static final int REQUEST_CODE_ALARMRECIEVER = 999;
    private EditText editTxtMessage;
    private TextView txtDate;
    private TextView txtTime;
    private Button btnDate;
    private Button btnTime;
    private ImageButton btnSend;
    private String name;
    private String number;
    private MyDate mDate;
    private MyTime mTime;
    private int broadcastId ;
    private int SmsID;
    ArrayList<MyContact> results;
    private int groupId;
    private SqlCommunication mSqlCommunication;

    private static boolean AirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_message);
        mDate = new MyDate();
        mTime = new MyTime();
        results = new ArrayList<>();
        editTxtMessage = findViewById(R.id.edit_txxMessage);
        btnSend = findViewById(R.id.btn_send);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time_message);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        mSqlCommunication = new SqlCommunication(this);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new DateFragment();
                fragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new TimeFragment();
                fragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        Intent intent = getIntent();

        if (intent != null && intent.getAction().equals(MainActivity.SENDMESSAGE_TO_GROUP_ACTION)) {
            results = intent.getParcelableArrayListExtra("group_member");
            groupId = intent.getIntExtra("group-id",-1);

            Toast.makeText(this, String.valueOf(groupId), Toast.LENGTH_SHORT).show();
        }else if(intent.getAction().equals(SelelctForMessageActivity.SELECT_FOR_MESSAGE_ACTIVITY)){
            Bundle bundle = intent.getBundleExtra("bundle");
            name  = bundle.getString("name");
            number = bundle.getString("number");
        }else if (intent != null && intent.getAction().equals(MainActivity.SENDBROADCAST_TO_GROUP_ACTION)) {
            results = intent.getParcelableArrayListExtra("selected-contacts");

            broadcastId = groupId+50;
            mSqlCommunication.insertContactsIntoGroup(results,broadcastId);
            groupId = broadcastId;
        }
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

    private boolean validateSelectedDateTime() {
        boolean validDateTime;
        Date currentDateTime = Calendar.getInstance().getTime();
        Date selectedDateTime = convertSelectedDateTime();

        if (selectedDateTime.compareTo(currentDateTime) < 0) {
            validDateTime = FALSE;
        } else {
            validDateTime = TRUE;
        }
        return validDateTime;
    }

    public void getDatePickerResult(int year, int month, int day) {
        mDate.setYear(year);
        mDate.setMonth(month);
        mDate.setDay(day);
        txtDate.setText(mDate.toString());
    }

    public void getTimePickerResult(int hours, int minutes) {
        mTime.setHour(hours);
        mTime.setMinute(minutes);
        txtTime.setText(mTime.toString());
    }

    private Date convertSelectedDateTime() {
        Date convertedDateTime = null;
        String selectedDateTime = "";

        int selectedMonth = (mDate.getMonth() + 1);

        selectedDateTime += mDate.getYear() + "" + "" + String.format("%02d", selectedMonth) + "" +
                String.format("%02d", mDate.getDay()) + "" + String.format("%02d", mTime.getHour()) + "" + String.format("%02d", mTime.getMinute());

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            convertedDateTime = sdf.parse(selectedDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateTime;
    }


    private void validateInput() {
        // Assign input to variables

        final String messageText = editTxtMessage.getText().toString();

        if (messageText.isEmpty()) // Ensure message is not empty.
        {
            Toast.makeText(getApplicationContext(), "Please enter a message.", Toast.LENGTH_SHORT).show();
        } else if (mDate.getDay() == -1 || mDate.getMonth() == -1 || mDate.getYear() == -1)  // Ensure date has been selected
        {
            Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_SHORT).show();
        } else if (mTime.getHour() == -1 || mTime.getMinute() == -1) // Ensure a time has been selected.
        {
            Toast.makeText(getApplicationContext(), "Please select a time", Toast.LENGTH_SHORT).show();
        } else if (validateSelectedDateTime() == FALSE) // Compare dates, compare to returns negative number if selected date is less than current date
        {
            Toast.makeText(getApplicationContext(), "SMS must be scheduled for a future time", Toast.LENGTH_SHORT).show();

        } else if (AirplaneModeOn(getApplicationContext()) == TRUE) // Check if airplane mode is on
        {
            // Options for dialog
            String[] options = {"Continue to schedule", "Do not schedule", "Cancel"};

            // Build dialog, set title and items as options
            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleMessageActivity.this);
            builder.setTitle("SMS will not send in airplane mode. Please select an option:");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int selectedOption) {
                    // listen for selected item, check selected item and perform appropriate action
                    if (selectedOption == 0) {
                        if (validateSelectedDateTime() == FALSE)  // Ensure time has not changed into past
                        {
                            Toast.makeText(getApplicationContext(), "Time has changed, SMS must be scheduled for a future time", Toast.LENGTH_SHORT).show();
                        } else {
                            createMessage(groupId, messageText);
                        }
                    } else if (selectedOption == 1) {
                        Toast.makeText(ScheduleMessageActivity.this, "SMS has not been scheduled", Toast.LENGTH_LONG).show();
                        resetInput();
                    } else if (selectedOption == 2) {
                        //Do nothing as user has canceled

                    } else {
                        Toast.makeText(ScheduleMessageActivity.this, "Sorry an error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
        } else //Schedule SMS
        {
            createMessage(groupId,  messageText);
        }
    }


    public void createMessage(int groupId, String messageText) {
        String gid = String.valueOf(groupId);
        String message = messageText;
        String messageDate = mDate.getDay() + "/" + mDate.getMonth() + "/" + mDate.getYear();
        String messageTime = String.format("%02d:%02d", mTime.getHour(), mTime.getMinute());
        String messageStatus = String.valueOf(SmsContract.STATUS_SCHEDULED);

        ScheduleSmsAsyncTask task = new ScheduleSmsAsyncTask();
        task.execute(gid, message, messageDate, messageTime, messageStatus);
    }

    private void resetInput() {
        editTxtMessage.setText("");
        txtDate.setText(R.string.no_date_selected);
        txtTime.setText(R.string.no_time_selected);
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_SMS);
    }

    private class ScheduleSmsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... string) {
            String result = "SMS Successfully Scheduled";
            try {

                int SmsID = mSqlCommunication.insertMessage(new MyMessage(string[0],string[1],string[2],string[3],string[4]),0);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, mDate.getYear());
                c.set(Calendar.MONTH, mDate.getMonth());
                c.set(Calendar.DAY_OF_MONTH, mDate.getDay());
                c.set(Calendar.HOUR_OF_DAY, mTime.getHour());
                c.set(Calendar.MINUTE, mTime.getMinute());
                c.set(Calendar.SECOND, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("SmsID", SmsID);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE_ALARMRECIEVER, intent, 0);
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "SMS failed to schedule";
            }
            return result;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(ScheduleMessageActivity.this, result, Toast.LENGTH_SHORT).show();
            resetInput();
        }
    }
    public void sendMySMS(String phoneNumber, String message, String name) {
        SmsManager sms = SmsManager.getDefault();
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            Intent intent = new Intent(this, NotifyUser.class);

            intent.putExtra("name", name);
            intent.putExtra("SmsID", SmsID);

            PendingIntent sentIntent = PendingIntent.getBroadcast(this, REQUEST_CODE_NOTIFY_USER, intent, 0);
            sms.sendTextMessage(phoneNumber, null, msg, sentIntent, null);
        }
    }

}