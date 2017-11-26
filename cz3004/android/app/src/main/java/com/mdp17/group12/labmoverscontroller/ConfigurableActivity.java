package com.mdp17.group12.labmoverscontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mdp17.group12.labmoverscontroller.enumType.BluetoothState;
import com.mdp17.group12.labmoverscontroller.menu.CustomListAdapter;
import com.mdp17.group12.labmoverscontroller.menu.CustomMenuItem;
import com.mdp17.group12.labmoverscontroller.util.Constant;

public class ConfigurableActivity extends MainActivity {

    private EditText mConfigButton1, mConfigButton2;
    private Button mReconfigure, mButton1, mButton2;
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mEditor = null;
    private String button1, button2;
    private BluetoothService mBluetoothService;

    private final String TAG = "ConfigurableActivity: ";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurable);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new CustomListAdapter(this, Constant.drawerItemTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });


        mTitle = mDrawerTitle = getTitle();
        if (Constant.LOG) {
            Log.d(TAG, "Initial Title: " + mTitle);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mConfigButton1 = (EditText) findViewById(R.id.configButton1);
        mConfigButton2 = (EditText) findViewById(R.id.configButton2);
        mReconfigure = (Button) findViewById(R.id.reconfigButton);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mSharedPreferences = getSharedPreferences("Reconfigurables", 0);
        button1 = mSharedPreferences.getString("button1", "BUTTON 1");
        button2 = mSharedPreferences.getString("button2", "BUTTON 2");
        mConfigButton1.setText(button1);
        mConfigButton2.setText(button2);
        mButton1.setText(button1);
        mButton2.setText(button2);
        mEditor = mSharedPreferences.edit();
        mBluetoothService = BluetoothService.getInstance(this);
    }

    protected void onStart()
    {
        super.onStart();
        mReconfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1 = mConfigButton1.getText().toString();
                button2 = mConfigButton2.getText().toString();
                mButton1.setText(button1);
                mButton2.setText(button2);
            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(button1);
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(button2);
            }
        });
    }

    private void sendMessage(String message)
    {
        if (mBluetoothService.getState() != BluetoothState.CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);
        }
    }

    protected void onStop()
    {
        super.onStop();
        mEditor.putString("button1", button1);
        mEditor.putString("button2", button2);
        mEditor.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call supportInvalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        if (Constant.LOG) {
            Log.d(TAG, "Current Position: " + position);
        }
        Intent i;
        CustomMenuItem item = Constant.drawerItemTitles.get(position);
        switch (item) {
            case INTERACTIVE:
                i = new Intent(this, CustomMenuItem.INTERACTIVE.getIntent());
                startActivity(i);
                break;
            case LEADERBOARD:
                i = new Intent(this, CustomMenuItem.LEADERBOARD.getIntent());
                startActivity(i);
                break;
            case SCAN_DEVICE:
                i = new Intent(this, CustomMenuItem.SCAN_DEVICE.getIntent());
                startActivity(i);
                break;
            case CONFIGURABLES:
                i = new Intent(this, CustomMenuItem.CONFIGURABLES.getIntent());
                startActivity(i);
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
