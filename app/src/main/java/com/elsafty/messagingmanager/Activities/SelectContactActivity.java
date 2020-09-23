package com.elsafty.messagingmanager.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.elsafty.messagingmanager.Adapters.ContactAdapter;
import com.elsafty.messagingmanager.Pojos.Contact;
import com.elsafty.messagingmanager.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectContactActivity extends AppCompatActivity implements ContactAdapter.OnItemClickListener {
    public static final String NAME_KEY = "name";
    public static final String NUMBER_KEY = "number";
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private ArrayList<Contact> mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        mRecyclerView = findViewById(R.id.recyclerView);
        mContacts = getContacts();
        mAdapter = new ContactAdapter(this,mContacts,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
    }
    private ArrayList<Contact> getContacts() {
        // Retrieve Contacts from phone
        String[] projections = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor result = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projections, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        // Create list of contact objects
        ArrayList<Contact> contact = new ArrayList<Contact>();

        // For number of contacts create a contact object with name and number.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            contact.add(new Contact(result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                    result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))));
        }
        return contact;
    }

    @Override
    public void onClick(String name, String phone) {
        Intent intent = new Intent(this,NewMessageActivity.class);
        intent.putExtra(NAME_KEY,name);
        intent.putExtra(NUMBER_KEY,phone);
        startActivity(intent);
    }
}