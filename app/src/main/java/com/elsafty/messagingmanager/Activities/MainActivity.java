package com.elsafty.messagingmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.elsafty.messagingmanager.Adapters.GroupAdapter;
import com.elsafty.messagingmanager.Adapters.SwipeToDeleteCallback;
import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.Pojos.MyGroup;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GroupAdapter.OnClickListener {
    public static final String MAINACTIVITY_ACTION = "main-activity";
    public static final String SEND_MESSAGE_TO_GROUP_ACTION = "send_group";
    public static final String SEND_NEWMESSAGE_ACTION = "send_new_message";
    public static final String SEND_BROADCAST_ACTION = "send_broadcast";
    public static final String SEND_SCHEDULE_MESSAGE_ACTION = "send_schedule_message";
    private static final int REQUEST_READ_CONTACTS = 1;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private SqlCommunication sqlCommunication;
    private RecyclerView recyclerView;
    private LinearLayout newMessageFab, newScheduleFab, broadCastFab;
    private GroupAdapter mAdapter;
    private List<MyGroup> items;
    private boolean isFABOpen;
    private int groupId;
    private ItemTouchHelper touchHelper;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        intiComponent();

        sqlCommunication = new SqlCommunication(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = sqlCommunication.getAllGroups();
        mAdapter = new GroupAdapter(this, items, this);
        touchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        recyclerView.setAdapter(mAdapter);
        touchHelper.attachToRecyclerView(recyclerView);
        newMessageFab.setVisibility(View.INVISIBLE);
        newScheduleFab.setVisibility(View.INVISIBLE);
        broadCastFab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int ReadContactsPermission = checkSelfPermission(READ_CONTACTS);
                if (ReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestContactPermission();
                } else {
                    readFromSharedPref();
                    if (!isFABOpen) {
                        showFABMenu();
                    } else {
                        closeFABMenu();
                    }
                }
            }
        });
        newScheduleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectContactActivity.class);
                intent.setAction(SEND_SCHEDULE_MESSAGE_ACTION);
                startActivity(intent);
            }
        });
        newMessageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectContactActivity.class);
                intent.setAction(SEND_NEWMESSAGE_ACTION);
                startActivity(intent);
            }
        });
        broadCastFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectContactActivity.class);
                intent.setAction(SEND_BROADCAST_ACTION);
                startActivity(intent);
            }
        });
        mToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }


    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Groups");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void intiComponent() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        newScheduleFab = findViewById(R.id.newschedule_fab);
        newMessageFab = findViewById(R.id.newMsseage_fab);
        broadCastFab = findViewById(R.id.broadCast_fab);

        recyclerView = findViewById(R.id.group_recyclerview);
        fab = findViewById(R.id.select_fab);
    }

    private void showFABMenu() {
        isFABOpen = true;

        fab.animate().rotation(180).withStartAction(new Runnable() {
            @Override
            public void run() {
                fab.setImageResource(R.drawable.ic_close);
                newMessageFab.setVisibility(View.VISIBLE);
                newScheduleFab.setVisibility(View.VISIBLE);
                broadCastFab.setVisibility(View.VISIBLE);
                newMessageFab.animate().translationX(-getResources().getDimension(R.dimen.tranX));
                newScheduleFab.animate().translationX(-getResources().getDimension(R.dimen.tranX));
                broadCastFab.animate().translationX(-getResources().getDimension(R.dimen.tranX));
            }
        });

        newMessageFab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        newScheduleFab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        broadCastFab.animate().translationY(-getResources().getDimension(R.dimen.standard_155));

    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab.animate().rotation(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                newMessageFab.setVisibility(View.INVISIBLE);

                newScheduleFab.setVisibility(View.INVISIBLE);

                broadCastFab.setVisibility(View.INVISIBLE);
                fab.setImageResource(R.drawable.ic_add_message);
            }
        });

        newMessageFab.animate().translationY(0);
        newScheduleFab.animate().translationY(0);
        broadCastFab.animate().translationY(0);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_group:
                Intent newGroupIntent = new Intent(this, NewGroupActivity.class);
                newGroupIntent.setAction(MAINACTIVITY_ACTION);
                startActivity(newGroupIntent);
                return true;
            case R.id.action_sent:
                Intent sentIntent = new Intent(this, SentActivity.class);
                sentIntent.setAction(MAINACTIVITY_ACTION);
                startActivity(sentIntent);
                return true;
            case R.id.action_schedule:
                Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
                scheduleIntent.setAction(MAINACTIVITY_ACTION);
                startActivity(scheduleIntent);
                return true;
            case R.id.action_trash:
                Intent trashIntent = new Intent(this, TrashActivity.class);
                trashIntent.setAction(MAINACTIVITY_ACTION);
                startActivity(trashIntent);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "this is settings item", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void requestContactPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_READ_CONTACTS:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    selectContact();
                } else {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                            showMessageOKCancel("You must allow this permission to select a contact",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestContactPermission();
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectContact() {
        Intent intent = new Intent(this, ScheduleMessageActivity.class);
        intent.setAction(MAINACTIVITY_ACTION);
        startActivity(intent);
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onItemClick(View view, MyGroup obj, int pos) {
        groupId = pos + 1;
        ArrayList<MyContact> contactInGroup = sqlCommunication.getAllContactsInTheSameGroup(groupId);
        Intent intent = new Intent(MainActivity.this, ScheduleMessageActivity.class);
        intent.setAction(SEND_MESSAGE_TO_GROUP_ACTION);
        intent.putParcelableArrayListExtra("group_member", contactInGroup);
        intent.putExtra("group-id", groupId);
        startActivity(intent);
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

    private void readFromSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("contactsPref", 0);
        boolean isFirst = sharedPreferences.getBoolean("first-time", false);
        if (isFirst == false) {
            new InsertAllContacts().execute(getContacts());
            writeInSharedPref(true);
        }
    }

    class InsertAllContacts extends AsyncTask<ArrayList<MyContact>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<MyContact>... arrayLists) {
            SqlCommunication sqlCommunication = new SqlCommunication(MainActivity.this);
            sqlCommunication.insertAllContacts(arrayLists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Contacts loaded Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}


