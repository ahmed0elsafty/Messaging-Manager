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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elsafty.messagingmanager.BroadcastReciever.AlarmReceiver;
import com.elsafty.messagingmanager.Fragments.DateFragment;
import com.elsafty.messagingmanager.Fragments.TimeFragment;
import com.elsafty.messagingmanager.Pojos.MyDate;
import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.Pojos.MyTime;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SmsContract;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import static android.Manifest.permission.SEND_SMS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class NewMessageActivity extends AppCompatActivity {
    private static final int REQUEST_SMS = 1;
    private EditText editTxtMessage;
    private TextView txtDate;
    private TextView txtTime;
    private Button btnDate;
    private Button btnTime;
    private ImageButton btnSend;
    private String name;
    private String number;
    private String message;
    private MyDate mDate;
    private MyTime mTime;
    private SqlCommunication mSqlCommunication;

    private static boolean AirplaneModeOn(Context context) {
        // Return true if airplane more is on
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        mDate = new MyDate();
        mTime = new MyTime();
        editTxtMessage = findViewById(R.id.edit_txxMessage);
        btnSend = findViewById(R.id.btn_send);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        mSqlCommunication  = new SqlCommunication(this);
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

        if (intent != null && intent.hasExtra(SelectContactActivity.NAME_KEY) && intent.hasExtra(SelectContactActivity.NUMBER_KEY)) {
            name = intent.getStringExtra(SelectContactActivity.NAME_KEY);
            number = intent.getStringExtra(SelectContactActivity.NUMBER_KEY);
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    // Check permission status of SEND_SMS
                    int SMSPermission = checkSelfPermission(SEND_SMS);
                    // If permission is not granted display message informing user the application requires permission
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
        Boolean validDateTime;
        // Get current date and time
        Date currentDateTime = Calendar.getInstance().getTime();
        // Get converted date and time
        Date selectedDateTime = convertSelectedDateTime();

        if (selectedDateTime.compareTo(currentDateTime) < 0) // Compare dates, returns negative number if selected date is less than current date
        {
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

        // Add 1 to month as months are between 0-11
        int selectedMonth = (mDate.getMonth() + 1);

        // Join integers and convert to String
        selectedDateTime += mDate.getYear() + "" + "" + String.format("%02d", selectedMonth) + "" +
                String.format("%02d", mDate.getDay()) + "" + String.format("%02d", mTime.getHour()) + "" + String.format("%02d", mTime.getMinute());

        try {
            //Convert String to date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            convertedDateTime = sdf.parse(selectedDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateTime;
    }

    private void validateInput() {
        message = editTxtMessage.getText().toString();
        if (name.isEmpty() & number.isEmpty()) // Ensure name is not empty
        {
            Toast.makeText(getApplicationContext(), "Please Select a contact.", Toast.LENGTH_SHORT).show();
        } else if (message.isEmpty()) // Ensure message is not empty.
        {
            Toast.makeText(getApplicationContext(), "Please enter a message.", Toast.LENGTH_SHORT).show();
        } else if (mDate.getDay() == -1 || mDate.getMonth() == -1 || mDate.getYear() == -1)  // Ensure date has been selected
        {
            Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_SHORT).show();
        } else if (mTime.getMinute() == -1 || mTime.getHour() == -1) // Ensure a time has been selected.
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                            addToSms(name, number, message);
                        }
                    } else if (selectedOption == 1) {
                        Toast.makeText(NewMessageActivity.this, "SMS has not been scheduled", Toast.LENGTH_LONG).show();
                        resetInput();
                    } else if (selectedOption == 2) {
                        //Do nothing as user has canceled

                    } else {
                        Toast.makeText(NewMessageActivity.this, "Sorry an error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
        } else //Schedule SMS
        {
            addToSms(name, number, message);
        }
    }
    public void addToSms(String contactName, String phoneNumber, String messageText) {
        String name = contactName;
        String number = phoneNumber;
        String message = messageText;
        String messageDate = mDate.getDay() + "/" + mDate.getMonth() + "/" + mDate.getYear(); //Convert integers to string
        String messageTime = String.format("%02d:%02d", mTime.getHour(), mTime.getMinute()); // Convert to String and format hours and minutes
        String messageStatus = String.valueOf(SmsContract.STATUS_SCHEDULED);

        // Start multi-thread to insert sms to database and start alarm manager
        ScheduleSmsAsyncTask task = new ScheduleSmsAsyncTask();
        task.execute(name, number, messageDate, messageTime, message, messageStatus);
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
                // Construct a Sms object and pass it to the helper for database insertion
                int SmsID = mSqlCommunication.insertMessage(new MyMessage(string[0], string[1], string[2], string[3], string[4], string[5]));

                // Create calendar with selected date and time
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, mDate.getYear());
                c.set(Calendar.MONTH, mDate.getMonth());
                c.set(Calendar.DAY_OF_MONTH, mDate.getDay());
                c.set(Calendar.HOUR_OF_DAY, mTime.getHour());
                c.set(Calendar.MINUTE, mTime.getMinute());
                c.set(Calendar.SECOND, 0);

                // Create alarm manager
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Pass SmsID to AlarmReceiver class
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("SmsID", SmsID);

                //Set SmsID as unique id, Set time to calender, Start alarm
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), SmsID, intent, 0);
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
            // Display result message
            Toast.makeText(NewMessageActivity.this, result, Toast.LENGTH_SHORT).show();

            // Clear Sms Fields
            resetInput();
        }
    }

}