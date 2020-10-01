package com.elsafty.messagingmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elsafty.messagingmanager.Adapters.MembersAdapter;
import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.Pojos.MyGroup;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewGroupActivity extends AppCompatActivity {
    public static final String NEWGROUPACTIVITY_ACTION = "newGroup-acivity";
    private TextView txtMemberCount;
    private RecyclerView recyclerView;
    private EditText editGroupName;
    private MembersAdapter mAdapter;
    private Button btnContacts;
    private SqlCommunication sqlCommunication;
    private ArrayList<MyContact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        sqlCommunication = new SqlCommunication(this);
        btnContacts = findViewById(R.id.btnselect_contact);
        txtMemberCount = findViewById(R.id.txt_member_count);
        editGroupName = findViewById(R.id.edit_group_name);
        recyclerView = findViewById(R.id.members_recycler_view);
        txtMemberCount = findViewById(R.id.txt_member_count);
        Intent intent = getIntent();
        contacts = new ArrayList<>();
        if (intent.getAction().equals(SelectContactActivity.SELECT_CONTACT_ACTION)){
            contacts = intent.getParcelableArrayListExtra("selected-contacts");
            txtMemberCount.setText(getResources().getQuantityString(R.plurals.numberOfMembers,contacts.size(),contacts.size()));
        }

        mAdapter = new MembersAdapter(this,contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewGroupActivity.this,SelectContactActivity.class);
                intent.setAction(NEWGROUPACTIVITY_ACTION);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton floatingActionButton=findViewById(R.id.group_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editGroupName.getText().toString().equals("")){
                    Toast.makeText(NewGroupActivity.this, "Required group name", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveGroup(editGroupName.getText().toString(),contacts);

                    finish();
                }
            }
        });

    }
    private void saveGroup(String groupName,ArrayList<MyContact> contacts){
        MyGroup group=new MyGroup(groupName,contacts.size());
        int id =  sqlCommunication.insertGroup(group);
        sqlCommunication.insertContactsIntoGroup(contacts,id);
        Toast.makeText(this, String.valueOf(group.getMembers()), Toast.LENGTH_SHORT).show();
    }


}