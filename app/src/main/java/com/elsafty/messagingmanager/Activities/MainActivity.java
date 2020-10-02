package com.elsafty.messagingmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.elsafty.messagingmanager.Adapters.GroupAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , GroupAdapter.OnClickListener {
    public static final String MAINACTIVITY_ACTION = "main-activity";
    public static final String SENDMESSAGE_TO_GROUP_ACTION = "send_group";
    private static final int REQUEST_READ_CONTACTS = 1;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private SqlCommunication sqlCommunication;
    private RecyclerView recyclerView;
    private FloatingActionButton newMessageFab,newScheduleFab;
    private GroupAdapter mAdapter;
    private List<MyGroup> items;
    private boolean isFABOpen;
    private int groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Groups");
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        sqlCommunication = new SqlCommunication(this);
        newScheduleFab = findViewById(R.id.newschedule_fab);
        newMessageFab = findViewById(R.id.newMsseage_fab);
        recyclerView = findViewById(R.id.group_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = sqlCommunication.getAllGroups();
        mAdapter = new GroupAdapter(this, items,this);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.select_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int ReadContactsPermission = checkSelfPermission(READ_CONTACTS);
                    if (ReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                        requestContactPermission();
                    } else {
                        if(!isFABOpen){
                            showFABMenu();
                        }else{
                            closeFABMenu();
                        }
                    }
                }
            }
        });
        newScheduleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "schedule", Toast.LENGTH_SHORT).show();
                selectContact();
            }
        });
        newMessageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectContact();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.newMessage){

            ArrayList<MyContact> contactInGroup = sqlCommunication.getAllContactsInTheSameGroup(groupId);
            Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
            intent.setAction(SENDMESSAGE_TO_GROUP_ACTION);
            intent.putParcelableArrayListExtra("group_member",contactInGroup);
            intent.putExtra("group-id",groupId);
            startActivity(intent);
        }else {
            ArrayList<MyContact> contactInGroup = sqlCommunication.getAllContactsInTheSameGroup(groupId);
            Intent intent = new Intent(MainActivity.this, ScheduleMessageActivity.class);
            intent.setAction(SENDMESSAGE_TO_GROUP_ACTION);
            intent.putParcelableArrayListExtra("group_member",contactInGroup);
            intent.putExtra("group-id",groupId);
            startActivity(intent);
        }

        return super.onContextItemSelected(item);

    }

    private void showFABMenu(){
        isFABOpen=true;
        newMessageFab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        newScheduleFab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));

    }

    private void closeFABMenu(){
        isFABOpen=false;
        newMessageFab.animate().translationY(0);
        newScheduleFab.animate().translationY(0);
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
            case R.id.action_help:
                Toast.makeText(this, "this is help item", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, SelelctForMessageActivity.class);
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
        groupId = pos+1;
    }






    /*private void deleteGroups() {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
            sqlCommunication.deleteGroup(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }*/
}


