package com.mdp17.group12.labmoverscontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mdp17.group12.labmoverscontroller.enumType.BluetoothState;
import com.mdp17.group12.labmoverscontroller.menu.CustomListAdapter;
import com.mdp17.group12.labmoverscontroller.menu.CustomMenuItem;
import com.mdp17.group12.labmoverscontroller.util.Constant;

public class SendReceiveUI extends MainActivity {
    private BluetoothAdapter mBluetoothAdapter = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private BluetoothService mBluetoothService = null;
    private Button mSendButton, mDisconnectButton, mButton;
    private ListView mReceiveText;
    private ArrayAdapter<String> arrayAdapter;
    private EditText mSendText;
    private StringBuffer mOutStringBuffer;
    private String mConnectedDeviceName = null;
    private TextView mConnectedDevice = null;
    private Switch mBluetoothSwitch;
    private MenuItem mMenuItem = null;
    private MenuItem rpiSwitch = null;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private final String TAG = "SendReceiveUI: ";

    private static final int REQUEST_ENABLE_BT = 1;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    //private boolean bluetoothReconnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive_ui);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mSendButton = (Button) findViewById(R.id.sendButton);
        mReceiveText = (ListView) findViewById(R.id.receiveText);
        mSendText = (EditText) findViewById(R.id.sendText);
        mBluetoothSwitch = (Switch) findViewById(R.id.bluetoothSwitch);
        bluetoothEnability();
        mBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else
                {
                    mBluetoothService.resetConnection();
                    mBluetoothAdapter.disable();
                }
            }
        });
        mBluetoothService = BluetoothService.getInstance(getApplicationContext());
        /*IntentFilter mFilter = new IntentFilter(Constant.MESSAGE_READ);
        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        mFilter.addAction(Constant.MESSAGE_DEVICE_NAME);
        mFilter.addAction(Constant.MESSAGE_STATE_CHANGE);
        mFilter.addAction(Constant.MESSAGE_TOAST);
        mFilter.addAction(Constant.MESSAGE_WRITE);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBluetoothReceiver, mFilter);
        this.registerReceiver(reconnectReceiver, mFilter2);*/

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
                //supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                //supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        Log.d(TAG, "mReconnectionState - " + Boolean.toString(mBluetoothService.getReconnectionState()));
        if(mBluetoothService.getReconnectionState())
        {
            Log.d(TAG, "mReconnectionState - reconnecting - " + Boolean.toString(mBluetoothService.getReconnectionState()));
            mBluetoothService.reconnect();
        }
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
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scanREQUEST_CONNECT_DEVICE_SECURE);
                Intent intent = new Intent(getApplicationContext(), ScanDeviceUI.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent intent = new Intent(getApplicationContext(), ScanDeviceUI.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call supportInvalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        super.onPrepareOptionsMenu(menu);
        if(mBluetoothService.getState() == BluetoothState.CONNECTED)
        {
            if (Constant.LOG) {
                Log.d(TAG, "bluetooth menu: " + false);
            }
            menu.findItem(R.id.secure_connect_scan).setEnabled(false);
            menu.findItem(R.id.insecure_connect_scan).setEnabled(false);
        }
        else
        {
            if (Constant.LOG) {
                Log.d(TAG, "bluetooth menu: " + true);
            }
            menu.findItem(R.id.secure_connect_scan).setEnabled(true);
            menu.findItem(R.id.insecure_connect_scan).setEnabled(true);
        }
        return true;
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

    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart");
        if (Constant.LOG) {
            Log.d(TAG, "statuss: " + mBluetoothService.getState());
        }
        setStatus(mBluetoothService.getState());
        IntentFilter mFilter = new IntentFilter(Constant.MESSAGE_READ);
        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        mFilter.addAction(Constant.MESSAGE_DEVICE_NAME);
        mFilter.addAction(Constant.MESSAGE_STATE_CHANGE);
        mFilter.addAction(Constant.MESSAGE_TOAST);
        mFilter.addAction(Constant.MESSAGE_WRITE);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBluetoothReceiver, mFilter);
        this.registerReceiver(reconnectReceiver, mFilter2);

        if(mBluetoothService == null)
        {
            setupChat();
        }
    }


    public void onResume()
    {
        super.onResume();
        bluetoothEnability();
        if(mBluetoothAdapter.isEnabled())
        {
            if(mBluetoothService != null)
            {
                if (Constant.LOG) {
                    Log.d(TAG, "onResume: " + mBluetoothService.getState());
                }
                if(mBluetoothService.getState() ==  BluetoothState.NONE)
                    mBluetoothService.listen();
            }
            else
                setupChat();
        }
    }

    public void bluetoothEnability()
    {
        if(!mBluetoothAdapter.isEnabled())
        {
            mBluetoothSwitch.setChecked(false);
        }
        else
        {
            mBluetoothSwitch.setChecked(true);
            setupChat();
        }
    }

    private void setStatus(BluetoothState status) {
        ActionBar mActionBar = getSupportActionBar();
        switch(status)
        {
            case NONE:
            case LISTENING:
                mActionBar.setSubtitle("not connected");
                break;
            case CONNECTING:
                mActionBar.setSubtitle("connecting...");
                break;
            case CONNECTED:
                mActionBar.setSubtitle("connected to " + mBluetoothService.getConnectedDevice().getName());
                //bluetoothReconnect = true;
                mBluetoothService.setReconnectionState(true);
                break;
        }
    }

    private final BroadcastReceiver reconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "RECONNECT---------------------------" + Boolean.toString(mBluetoothService.getReconnectionState()));
            if(mBluetoothService.getReconnectionState())
                mBluetoothService.reconnect();
        }
    };

    private final BroadcastReceiver localBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.LOG) {
                Log.d("SendReceiveUI: ", action);
            }
            if(Constant.MESSAGE_READ.equals(action))
            {
                /*byte[] readBuf = intent.getExtras().getByteArray(Constant.EXTRA_BUFFER);
                int numBytes = intent.getExtras().getInt(Constant.EXTRA_NUMBYTES);
                String readMessage = new String(readBuf, 0, numBytes);*/
                //byte[] readBuf = intent.getExtras().getByteArray(Constant.EXTRA_BUFFER);
                //int numBytes = intent.getExtras().getInt(Constant.EXTRA_NUMBYTES);
                //String readMessage = new String(readBuf, 0, numBytes);
                String readMessage = intent.getExtras().getString(Constant.EXTRA_STRING);
                arrayAdapter.add(readMessage);
                mReceiveText.smoothScrollToPosition(arrayAdapter.getCount());
                arrayAdapter.notifyDataSetChanged();
            }
            else if(Constant.MESSAGE_WRITE.equals(action))
            {
                byte[] writeBuf = intent.getExtras().getByteArray(Constant.EXTRA_BUFFER);
                String writeMessage = new String(writeBuf);
            }
            else if(Constant.MESSAGE_DEVICE_NAME.equals(action))
            {
                mConnectedDeviceName = intent.getExtras().getString(Constant.EXTRA_STRING);
                //mConnectedDevice.setText("Connected to: " + mConnectedDeviceName);
                //mDisconnectButton.setEnabled(true);
                supportInvalidateOptionsMenu();
                mMenuItem.setVisible(true);
                mMenuItem.setEnabled(true);
            }
            else if(Constant.MESSAGE_TOAST.equals(action))
            {
                String message = intent.getExtras().getString(Constant.EXTRA_STRING);
                Toast.makeText(SendReceiveUI.this, message, Toast.LENGTH_SHORT).show();
                //mConnectedDevice.setText("Not connected");
                //mDisconnectButton.setEnabled(false);
                if(message == "Device connection was lost")
                {
                    supportInvalidateOptionsMenu();
                    mMenuItem.setVisible(false);
                    mMenuItem.setEnabled(false);
                    setupChat();
                }
            }
            else if(Constant.MESSAGE_STATE_CHANGE.equals(action))
            {
                int bluetoothCode = intent.getExtras().getInt(Constant.EXTRA_STRING);
                switch(BluetoothState.getEnum(bluetoothCode))
                {
                    case CONNECTED:
                        //setStatus("connected to " + mConnectedDeviceName);
                        setStatus(BluetoothState.CONNECTED);
                        if(arrayAdapter != null)
                            arrayAdapter.clear();
                        break;
                    case CONNECTING:
                        //setStatus("connecting...");
                        setStatus(BluetoothState.CONNECTING);
                        break;
                    case LISTENING:
                    case NONE:
                        //setStatus("not connected");
                        setStatus(BluetoothState.NONE);
                        break;
                }
            }
            /*else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
            {
                if (Constant.LOG) {
                    Log.d(TAG, "Reconnect");
                }
                if(bluetoothReconnect)
                    mBluetoothService.reconnect();
            }*/
        }
    };

    private void setupChat()
    {
        mOutStringBuffer = new StringBuffer("");
        arrayAdapter = new ArrayAdapter<>(SendReceiveUI.this, android.R.layout.simple_list_item_1);
        mReceiveText.setAdapter(arrayAdapter);
        mSendText.setOnEditorActionListener(mWriteListener);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) findViewById(R.id.sendText);
                String message = textView.getText().toString();
                sendMessage(message);
            }
        });

        //mBluetoothService.setHandler(mHandler);

        /*mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothService.resetConnection();
            }
        });*/
//
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SendReceiveUI.this, RobotStatus.class);
//                startActivity(intent);
//            }
//        });

        //mBluetoothService = new BluetoothService();
        //Intent intent = getIntent();
        //int index = intent.getIntExtra("device_index", 0);
        //BluetoothDevice bd = intent.getParcelableArrayExtra("bluetooth_device")[index];
        //mBluetoothService.connect(intent.getParcelableArrayExtra("bluetooth_device")[index]);
    }

    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if(i == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                String message = textView.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };


    private void sendMessage(String message)
    {
        if (mBluetoothService.getState() != BluetoothState.CONNECTED) {
            Toast.makeText(SendReceiveUI.this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mSendText.setText(mOutStringBuffer);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data != null)
        {
            switch(requestCode)
            {
                case REQUEST_CONNECT_DEVICE_SECURE:
                    if (resultCode == Activity.RESULT_OK) {
                        //bluetoothReconnect = true;
                        connectDevice(data, true);
                    }
                    break;
                case REQUEST_CONNECT_DEVICE_INSECURE:
                    if (resultCode == Activity.RESULT_OK) {
                        //bluetoothReconnect = true;
                        connectDevice(data, false);
                    }
                    break;
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString("device_address");
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, secure);
    }

    /*Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int) msg.arg1;
            int end = (int) msg.arg2;

            switch (msg.what) {
                case Constant.MESSAGE_WRITE:
                    String writeMessage = new String(writeBuf);
                    //writeMessage = writeMessage.substring(begin, end);
                    break;
                case Constant.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    arrayAdapter.add(readMessage);
                    mReceiveText.smoothScrollToPosition(arrayAdapter.getCount());
                    arrayAdapter.notifyDataSetChanged();
                    break;
                case Constant.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constant.DEVICE_NAME);
                    mConnectedDevice.setText("Connected to: " + mConnectedDeviceName);
                    mDisconnectButton.setEnabled(true);
                    break;
                case Constant.MESSAGE_TOAST:
                    Toast.makeText(SendReceiveUI.this, msg.getData().getString(Constant.TOAST), Toast.LENGTH_SHORT).show();
                    mConnectedDevice.setText("Not connected");
                    mDisconnectButton.setEnabled(false);
                    setupChat();
                    break;
            }

        }
    };*/

    private void ensureDiscoverable()
    {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bluetooth_menu, menu);
        mMenuItem = menu.findItem(R.id.disconnect);
        if(mBluetoothService.getState() == BluetoothState.CONNECTED)
        {
            mMenuItem.setVisible(true);
            mMenuItem.setEnabled(true);
        }
        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mBluetoothService.resetConnection();
                //bluetoothReconnect = false;
                mBluetoothService.setReconnectionState(false);
                return false;
            }
        });
        rpiSwitch = menu.findItem(R.id.rpi_select);
        if (Constant.RPI1_IN_USE) {
            rpiSwitch.setTitle(R.string.rpi1);
        } else {
            rpiSwitch.setTitle(R.string.rpi2);
        }
        if (Constant.LOG) {
            Log.d(TAG, "Current Rpi: " + Constant.RPI_MAC_ADDR);
        }
        rpiSwitch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (Constant.RPI1_IN_USE) {
                    Constant.RPI1_IN_USE = false;
                    Constant.RPI_MAC_ADDR = Constant.RPI2;
                    rpiSwitch.setTitle(R.string.rpi2);
                } else {
                    Constant.RPI1_IN_USE = true;
                    Constant.RPI_MAC_ADDR = Constant.RPI1;
                    rpiSwitch.setTitle(R.string.rpi1);
                }
                if (Constant.LOG) {
                    Log.d(TAG, "Current Rpi: " + Constant.RPI_MAC_ADDR);
                }
                return false;
            }
        });
        return true;
    }

    protected void onStop()
    {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBluetoothReceiver);
        unregisterReceiver(reconnectReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BluetoothService.onDestroy();
    }
}
