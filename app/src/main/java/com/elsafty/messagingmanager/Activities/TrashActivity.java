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

public class TrashActivity extends AppCompatActivity {
    private SmsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<RealMessage> messages;
    private SqlCommunication sqlCommunication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        sqlCommunication = new SqlCommunication(this);
        recyclerView = findViewById(R.id.trash_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        messages = new ArrayList<>();
        messages=sqlCommunication.getMessagesList(SmsContract.STATUS_FAILED);
        mAdapter = new SmsAdapter(TrashActivity.this, messages);
        recyclerView.setAdapter(mAdapter);
    }
}