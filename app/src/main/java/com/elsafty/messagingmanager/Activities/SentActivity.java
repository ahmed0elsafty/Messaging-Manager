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

public class SentActivity extends AppCompatActivity {
    private ArrayList<RealMessage> messages;
    private SmsAdapter mAdapter;
    private RecyclerView recyclerView;
    private SqlCommunication sqlCommunication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);
        sqlCommunication = new SqlCommunication(this);
        recyclerView = findViewById(R.id.sent_recycler_view);
        messages = sqlCommunication.getMessagesList(SmsContract.STATUS_SENT);
        mAdapter = new SmsAdapter(SentActivity.this, messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }
}