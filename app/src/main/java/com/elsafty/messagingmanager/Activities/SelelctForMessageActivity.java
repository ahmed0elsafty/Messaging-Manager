package com.elsafty.messagingmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.elsafty.messagingmanager.Adapters.MembersAdapter;
import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelelctForMessageActivity extends AppCompatActivity implements MembersAdapter.MyOnClickListener {
    public static final String SELECT_FOR_MESSAGE_ACTIVITY="select-for-message-activity";
    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    private ArrayList<MyContact> contacts;
    private SqlCommunication sqlCommunication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selelct_for_message);
        recyclerView = findViewById(R.id.select_recycler_view);
        sqlCommunication = new SqlCommunication(this);
        contacts = sqlCommunication.getAllContacts();
        mAdapter = new MembersAdapter(this,contacts,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(String name, String number,int id) {
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("number",number);
        bundle.putInt("id",id);
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NewMessageActivity.class);
        intent.putExtra("bundle",bundle);
        intent.setAction(SELECT_FOR_MESSAGE_ACTIVITY);
        startActivity(intent);
    }
}