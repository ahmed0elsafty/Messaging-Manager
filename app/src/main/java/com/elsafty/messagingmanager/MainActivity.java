package com.elsafty.messagingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_group:
                Intent newIntent = new Intent(this,newMessageActivity.class);
                startActivity(newIntent);
                return true;
            case R.id.action_sent:
                Intent sentIntent = new Intent(this,SentActivity.class);
                startActivity(sentIntent);
                return true;
            case R.id.action_schedule:
                Intent scheduleIntent = new Intent(this,ScheduleActivity.class);
                startActivity(scheduleIntent);
                return true;
            case R.id.action_trash:
//                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent trashIntent = new Intent(this,TrashActivity.class);
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
}