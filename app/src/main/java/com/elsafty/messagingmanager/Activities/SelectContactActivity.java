package com.elsafty.messagingmanager.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.elsafty.messagingmanager.Adapters.ContactAdapter;
import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;
import com.elsafty.messagingmanager.Utilities.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectContactActivity extends AppCompatActivity {
    public static final String SELECT_CONTACT_ACTION = "selectContact-activity";
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private ArrayList<MyContact> contacts;
    private boolean fromNewGroup = false;
    private boolean fromBroadCastAction = false;
    private boolean fromNewMessaageAction = false;
    private boolean fromScheduleMessageAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        initToolbar();
        initComponent();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case NewGroupActivity.NEWGROUPACTIVITY_ACTION:
                    fromNewGroup = true;
                    break;
                case MainActivity.SEND_BROADCAST_ACTION:
                    fromBroadCastAction = true;
                    break;
                case MainActivity.SEND_NEWMESSAGE_ACTION:
                    fromNewMessaageAction = true;
                    break;
                case MainActivity.SEND_SCHEDULE_MESSAGE_ACTION:
                    fromScheduleMessageAction = true;
                    break;

            }
        }


        SharedPreferences sharedPreferences = getSharedPreferences("contactsPref", 0);
        boolean isFirst = sharedPreferences.getBoolean("first-time", false);
        if (isFirst == false) {
            new InsertAllContacts().execute(getContacts());
            writeInSharedPref(true);
        }

        FloatingActionButton fab = findViewById(R.id.select_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contacts = getselectedContacts();
                if (fromNewGroup) {
                    Intent intent1 = new Intent(SelectContactActivity.this, NewGroupActivity.class);
                    intent1.putParcelableArrayListExtra("selected-contacts", contacts);
                    intent1.setAction(SELECT_CONTACT_ACTION);
                    startActivity(intent1);
                    finish();
                } else if (fromBroadCastAction) {
                    Intent intent1 = new Intent(SelectContactActivity.this, ScheduleMessageActivity.class);
                    intent1.putParcelableArrayListExtra("selected-contact", contacts);
                    intent1.setAction(SELECT_CONTACT_ACTION);
                    startActivity(intent1);
                    finish();
                }
            }
        });

        if (!(fromNewMessaageAction || fromScheduleMessageAction)) {
            Toast.makeText(this, "Long press for multi selection", Toast.LENGTH_SHORT).show();
        }else {
            fab.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.select_contact_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private ArrayList<MyContact> getselectedContacts() {
        ArrayList<MyContact> contacts = new ArrayList<>();
        for (int id : mAdapter.getSelectedItems()) {
            contacts.add(mAdapter.getItem(id));
        }
        return contacts;
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.contact_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        contacts = new ArrayList<>();
        contacts.addAll(getContacts());
        mAdapter = new ContactAdapter(this, contacts);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new ContactAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, MyContact obj, int pos) {
                if (mAdapter.getSelectedItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    MyContact contact = mAdapter.getItem(pos);
                    Intent intent ;
                    if (fromNewMessaageAction){
                         intent= new Intent(SelectContactActivity.this,NewMessageActivity.class);
                         intent.setAction(MainActivity.SEND_NEWMESSAGE_ACTION);
                         intent.putExtra("selected-contact",pos+1);
                         startActivity(intent);
                    }else if (fromScheduleMessageAction){
                        intent= new Intent(SelectContactActivity.this,ScheduleMessageActivity.class);
                        intent.setAction(MainActivity.SEND_SCHEDULE_MESSAGE_ACTION);
                        intent.putExtra("selected-contact",pos+1);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onItemLongClick(View view, MyContact obj, int pos) {
                if (fromNewMessaageAction || fromScheduleMessageAction) {
                    view.cancelLongPress();
                } else {
                    enableActionMode(pos);
                }
            }
        });

        actionModeCallback = new ActionModeCallback();

    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private ArrayList<MyContact> getContacts() {
        // Retrieve Contacts from phone
        String[] projections = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor result = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projections, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        // Create list of contacts objects
        ArrayList<MyContact> contacts = new ArrayList<MyContact>();

        // For number of contacts create a contacts object with name and number.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            contacts.add(new MyContact(result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
        }
        return contacts;
    }

    private void writeInSharedPref(boolean isFirstTime) {
        SharedPreferences preferences = getSharedPreferences("contactsPref", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("first-time", isFirstTime);
        edit.commit();
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(SelectContactActivity.this, R.color.colorDarkBlue2);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(SelectContactActivity.this, R.color.colorPrimary);
        }
    }

    class InsertAllContacts extends AsyncTask<ArrayList<MyContact>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<MyContact>... arrayLists) {
            SqlCommunication sqlCommunication = new SqlCommunication(SelectContactActivity.this);
            sqlCommunication.insertAllContacts(arrayLists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(SelectContactActivity.this, "Contacts loaded Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
