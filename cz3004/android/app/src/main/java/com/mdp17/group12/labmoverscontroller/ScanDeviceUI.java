package com.mdp17.group12.labmoverscontroller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mdp17.group12.labmoverscontroller.menu.CustomListAdapter;
import com.mdp17.group12.labmoverscontroller.menu.CustomMenuItem;
import com.mdp17.group12.labmoverscontroller.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class ScanDeviceUI extends MainActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mProgressDialog;
    private List<HashMap<String, String>> scandata, pairdata;
    private HashMap<String, String> scandatum, pairdatum;
    private ListView mScanView, mPairView;
    //private ConnectThread mConnectThread;;
    private Button mScan;
    private LinearLayout mGroupView;
    private BluetoothService mBluetoothService;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device_ui);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Scanning...");
        mProgressDialog.setCancelable(false);
        mGroupView = (LinearLayout) findViewById(R.id.bluetoothDevices);
        mScan = (Button) findViewById(R.id.scan);
        mScanView = (ListView) findViewById(R.id.scandevices);
        mPairView = (ListView) findViewById(R.id.paireddevices);
        scandata = new ArrayList<HashMap<String, String>>();
        pairdata = new ArrayList<HashMap<String, String>>();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        searchPairedDevices();
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
            }
        });
        mScanView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //mBluetoothService = BluetoothService.getInstance();
                //mBluetoothService.connect(btDevice.get(i));
                /*mConnectThread = new ConnectThread(btDevice.get(i));
                mConnectThread.start();*/
                Intent intent = new Intent();
                intent.putExtra("device_address", scandata.get(i).get("MAC"));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        mPairView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("device_address", pairdata.get(i).get("MAC"));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

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
        CustomMenuItem item = Constant.drawerItemTitles.get(position);         switch (item) {             case INTERACTIVE:                 i = new Intent(this, CustomMenuItem.INTERACTIVE.getIntent());                 startActivity(i);                 break;             case LEADERBOARD:                 i = new Intent(this, CustomMenuItem.LEADERBOARD.getIntent());                 startActivity(i);                 break;             case SCAN_DEVICE:                 i = new Intent(this, CustomMenuItem.SCAN_DEVICE.getIntent());                 startActivity(i);                 break;             case CONFIGURABLES:                 i = new Intent(this, CustomMenuItem.CONFIGURABLES.getIntent());                 startActivity(i);                 break;         }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public void onResume()
    {
        super.onResume();
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                scandata = new ArrayList<HashMap<String, String>>();
                mProgressDialog.show();
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                mProgressDialog.dismiss();
                SimpleAdapter adapter = new SimpleAdapter(ScanDeviceUI.this, scandata, android.R.layout.simple_list_item_2, new String[]{"Name", "MAC"}, new int[]{android.R.id.text1, android.R.id.text2});
                mScanView = (ListView) findViewById(R.id.scandevices);
                mScanView.setAdapter(adapter);

            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    String deviceName = device.getName();
                    String deviceMAC = device.getAddress();
                    scandatum = new HashMap<String, String>();
                    scandatum.put("Name", deviceName);
                    scandatum.put("MAC", deviceMAC);
                    scandata.add(scandatum);
                    if (Constant.LOG) {
                        Log.d(TAG, "device name: " + deviceName);
                    }
                }
            }
        }
    };

    void searchPairedDevices()
    {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        pairdata = new ArrayList<HashMap<String, String>>();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                String deviceName = device.getName();
                String deviceMAC = device.getAddress();
                pairdatum = new HashMap<String, String>();
                pairdatum.put("Name", deviceName);
                pairdatum.put("MAC", deviceMAC);
                pairdata.add(pairdatum);
            }
            SimpleAdapter adapter = new SimpleAdapter(ScanDeviceUI.this, pairdata, android.R.layout.simple_list_item_2, new String[]{"Name", "MAC"}, new int[]{android.R.id.text1, android.R.id.text2});
            mPairView.setAdapter(adapter);
        }
    }

    void doDiscovery()
    {
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
