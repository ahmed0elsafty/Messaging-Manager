package com.elsafty.messagingmanager.Activities;

import android.os.Bundle;

import com.elsafty.messagingmanager.Adapters.SmsAdapter;
import com.elsafty.messagingmanager.Pojos.RealMessage;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SmsContract;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleActivity extends AppCompatActivity {

    private SmsAdapter mAdapter;
    private RecyclerView recyclerView;
    private SqlCommunication sqlCommunication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        sqlCommunication = new SqlCommunication(this);
        recyclerView = findViewById(R.id.schedule_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ArrayList<RealMessage> realMessages = sqlCommunication.getMessagesList(SmsContract.STATUS_SCHEDULED);
        mAdapter = new SmsAdapter(ScheduleActivity.this, realMessages);
        recyclerView.setAdapter(mAdapter);

    }

}