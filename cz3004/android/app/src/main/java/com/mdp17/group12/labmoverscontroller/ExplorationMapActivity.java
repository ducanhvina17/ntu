package com.mdp17.group12.labmoverscontroller;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mdp17.group12.labmoverscontroller.behaviour.ResetDialogWrapper;
import com.mdp17.group12.labmoverscontroller.behaviour.ToastWrapper;
import com.mdp17.group12.labmoverscontroller.dialog.ResetDialogFragment;
import com.mdp17.group12.labmoverscontroller.enumType.BluetoothState;
import com.mdp17.group12.labmoverscontroller.mazeDrawer.MazeView;
import com.mdp17.group12.labmoverscontroller.menu.CustomListAdapter;
import com.mdp17.group12.labmoverscontroller.menu.CustomMenuItem;
import com.mdp17.group12.labmoverscontroller.enumType.CellStatus;
import com.mdp17.group12.labmoverscontroller.util.Constant;
import com.mdp17.group12.labmoverscontroller.enumType.Direction;
import com.mdp17.group12.labmoverscontroller.util.ReceiveCommand;
import com.mdp17.group12.labmoverscontroller.util.RemoteController;
import com.mdp17.group12.labmoverscontroller.enumType.ResetCode;

import java.util.Timer;
import java.util.TimerTask;

public class ExplorationMapActivity extends MainActivity implements ToastWrapper, ResetDialogWrapper {

    private final String TAG = "ExplorationActivity: ";
    private final String AUTO_REFRESH_ENABLE = "Auto-refresh Enabled";
    private final String AUTO_REFRESH_DISABLE = "Auto-refresh Disabled";
    private final String DEFAULT_COVERAGE_STRING = "No Coverage String Received!";
    private final String DEFAULT_OBSTACLE_STRING = "No Obstacle String Received";
//    private final String MD5_TAG = "md5_string";
//    private final String EXPLORATION_FINISHED = "exploration_finished";
//    private final String FASTEST_FINISHED = "fastest_finished";
//    private final String DELIM = " ";

    private static LocalBroadcastManager mBroadcaster = null;
    private static RemoteController rc;

    private MazeView mazeView;
    private TextView robotStatusView;

    private Button showMd5Button;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;
    private int width;
    private TextView coverageStringView;
    private TextView obstacleStringView;

    private RobotStatus isStarted = RobotStatus.STOPPED;
    private LeaderboardMode runMode = LeaderboardMode.EXPLORATION;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private BluetoothService mBluetoothService;

    public ReceiveCommand commandQueue = ReceiveCommand.DEFAULT_COMMAND;

    private String coverageString = DEFAULT_COVERAGE_STRING;
    private String obstacleString = DEFAULT_OBSTACLE_STRING;

    private TextView clockTimer;
    private Timer timer;
    private int timelapse = 0;
    private MenuItem playButton;

    @Override
    public void sendToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    private enum LeaderboardMode {
        EXPLORATION, FASTEST
    }
    private enum RobotStatus {
        RUNNING, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploration_map);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mBluetoothService = BluetoothService.getInstance(getApplicationContext());
        setStatus(mBluetoothService.getState());

        /*IntentFilter mFilter = new IntentFilter(Constant.MESSAGE_READ);
        mFilter.addAction(Constant.MESSAGE_STATE_CHANGE);
        mFilter.addAction(Constant.MESSAGE_TOAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBluetoothReceiver, mFilter);*/

        RelativeLayout mazeLayout = (RelativeLayout) findViewById(R.id.maze_layout);
        rc = new RemoteController(this);
        mazeView = new MazeView(this, 0, 0, Constant.MAZE_NO_PADDING, rc);
        mazeView.setOnTouchListener(new OnSwipeListener(ExplorationMapActivity.this) {
            public void onPress() {
                ReceiveCommand receiveCommand = commandQueue;
                update(receiveCommand);
                if (Constant.LOG) {
                    Log.d(TAG, "Flushed: " + receiveCommand.getStr());
                }
                sendToast(R.string.refresh_text);
            }
        });
        mazeLayout.addView(mazeView);

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

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_exploration_map);
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        showMd5Button = (Button) findViewById(R.id.show_md5);
        showMd5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.show_md5_layout, null);
                coverageStringView = (TextView) container.findViewById(R.id.coverage_string_value);
                obstacleStringView =(TextView) container.findViewById(R.id.obstacle_string_value);

                int popupWidth = (width * 3) / 4;
                int popupHeight = (width * 3) / 4;
                int popupXOffset = width / 8;
                int popupYOffset = width / 2;

                popupWindow = new PopupWindow(container, popupWidth, popupHeight, true);
                coverageStringView.setText(coverageString.toString());
                obstacleStringView.setText(obstacleString);
                popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, popupXOffset, popupYOffset);

                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        clockTimer = (TextView) findViewById(R.id.clock);
        timelapse = 0;

        if (Constant.LOG) {
            Log.d(TAG, "Reconnect State: " + mBluetoothService.getReconnectionState());
        }
        if(mBluetoothService.getReconnectionState())
            mBluetoothService.reconnect();
    }

    protected void onStart() {
        super.onStart();
        IntentFilter mFilter = new IntentFilter(Constant.MESSAGE_READ);
        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        mFilter.addAction(Constant.MESSAGE_STATE_CHANGE);
        mFilter.addAction(Constant.MESSAGE_TOAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBluetoothReceiver, mFilter);
        this.registerReceiver(reconnectReceiver, mFilter2);
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
            case R.id.refresh_button:
                ReceiveCommand receiveCommand = commandQueue;
                update(receiveCommand);
                if (Constant.LOG) {
                    Log.d(TAG, "Flushed: " + receiveCommand.getStr());
                }
                sendToast(R.string.refresh_text);
                break;
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

    private void setStatus(BluetoothState status) {
        final ActionBar mActionBar = getSupportActionBar();
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
                break;
        }
    }

    private final BroadcastReceiver reconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //mBluetoothService.reconnect();
            mBluetoothService.setReconnectionState(true);
            mBluetoothService.reconnect();
        }
    };

    private final BroadcastReceiver localBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Constant.MESSAGE_READ.equals(action)) {
                if (Constant.LOG) {
                    Log.d(TAG, "Map receive updates");
                }
                long start = System.nanoTime();
                String update = intent.getExtras().getString(Constant.EXTRA_STRING);
                ReceiveCommand receiveCommand = new ReceiveCommand(update);
                commandQueue = receiveCommand;
                if (Constant.AUTO_REFRESH_ENABLED) {
                    update(receiveCommand);
                }
                long end = System.nanoTime();
                if (Constant.LOG) {
                    Log.d(TAG, "Refresh Time: " + (end - start));
                }
            }
            else if(Constant.MESSAGE_TOAST.equals(action))
            {
                String message = intent.getExtras().getString(Constant.EXTRA_STRING);
                sendToast(message);
                //mConnectedDevice.setText("Not connected");
                //mDisconnectButton.setEnabled(false);
                if(message == "Device connection was lost")
                {
                    supportInvalidateOptionsMenu();
                    Log.d(TAG, "reconnecting");
                    //mBluetoothService.reconnect();
                    mBluetoothService.setReconnectionState(true);
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
        }
    };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.exploration_menu, menu);
        Switch autoUpdateSwitch = (Switch) menu.findItem(R.id.auto_refresh_toggle).getActionView();
        autoUpdateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Constant.AUTO_REFRESH_ENABLED = b;
                sendToast(b ? AUTO_REFRESH_ENABLE : AUTO_REFRESH_DISABLE);
            }
        });
        final MenuItem startFastest = menu.findItem(R.id.start_fastest);
        startFastest.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (isStarted == RobotStatus.RUNNING) {
                    stopClock();
                    resetClock();
                }
                if (!rc.startFastestRun()) {
                    clockTimer.setTextColor(Color.GREEN);
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        synchronized public void run() {
                            if (timelapse >= 3600) {
                                return;
                            }
                            int sec = timelapse % 60;
                            int min = timelapse / 60;
                            StringBuilder display = new StringBuilder();
                            if (min < 10) {
                                display.append('0');
                            }
                            display.append(min).append(":");
                            if (sec < 10) {
                                display.append('0');
                            }
                            display.append(sec);
                            final String currentlapse = display.toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clockTimer.setText(currentlapse);
                                }
                            });
                            timelapse++;
                        }
                    }, 0, 1000);
                    return true;
                }
                return true;
            }
        });
        final MenuItem startExploration = menu.findItem(R.id.exploration_start);
        playButton = startExploration;
        startExploration.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (isStarted == RobotStatus.RUNNING) {
                    if (!rc.backToStart()) {
                        return true;
                    }
                    stopClock();
                } else {
                    if (!rc.startExploration()) {
                        return true;
                    }
                    menuItem.setIcon(R.mipmap.ic_pause);
                    isStarted = RobotStatus.RUNNING;
                    clockTimer.setTextColor(Color.GREEN);
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        synchronized public void run() {
                            if (timelapse >= 3600) {
                                return;
                            }
                            int sec = timelapse % 60;
                            int min = timelapse / 60;
                            StringBuilder display = new StringBuilder();
                            if (min < 10) {
                                display.append('0');
                            }
                            display.append(min).append(":");
                            if (sec < 10) {
                                display.append('0');
                            }
                            display.append(sec);
                            final String currentlapse = display.toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clockTimer.setText(currentlapse);
                                }
                            });
                            timelapse++;
                        }
                    }, 0, 1000);
                }
                return true;
            }
        });
        MenuItem resetObstacles = menu.findItem(R.id.obstacles_reset);
        resetObstacles.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DialogFragment obstaclesReset = ResetDialogFragment.newInstance(R.string.obstacles_reset_title, ResetCode.OBSTACLES_RESET_CODE);
                obstaclesReset.show(getSupportFragmentManager(), TAG);
                return true;
            }
        });
        MenuItem resetRobot = menu.findItem(R.id.robot_reset);
        resetRobot.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DialogFragment robotReset = ResetDialogFragment.newInstance(R.string.robot_reset_title, ResetCode.ROBOT_RESET_CODE);
                robotReset.show(getSupportFragmentManager(), TAG);
                return true;
            }
        });
        return true;
    }

    @Override
    public void doPositiveClick(ResetCode action) {
        switch (action) {
            case ROBOT_RESET_CODE:
                if (rc.setCoordinate(0, 0)) {
                    mazeView.resetRobot();
                    if (Constant.LOG) {
                        Log.d(TAG, "Resetting Robot...");
                    }
                }
                break;
            case OBSTACLES_RESET_CODE:
                mazeView.resetObstacles();
                if (Constant.LOG) {
                     Log.d(TAG, "Resetting Obstacles...");
                }
                break;
        }
    }

    @Override
    public void doNegativeClick() {
        if (Constant.LOG) {
            Log.d(TAG, "Reset Cancelled");
        }
    }

    private void update(ReceiveCommand receiveCommand) {
        int x = receiveCommand.getX();
        int y = receiveCommand.getY();
        Direction dir = receiveCommand.getDir();
//        HashSet obstacles = receiveCommand.getObstacles();
        CellStatus[][] grid = receiveCommand.getGrid();
        String status = receiveCommand.getStatus();
        if (Constant.LOG) {
            Log.d(TAG, "updating:" + status);
        }
        String mdf1 = receiveCommand.getMdf1();
        String mdf2 = receiveCommand.getMdf2();
        setCoverageString(mdf1);
        setObstacleString(mdf2);
//        if (status.contains(MD5_TAG)) {
//            String[] md5 = status.split(DELIM);
//            if (md5.length != 3) {
//                return;
//            }
//            if (Constant.LOG) {
//                Log.d(TAG, "COVERAGE STRING: " + md5[1]);
//                Log.d(TAG, "OBSTACLE STRING: " + md5[2]);
//            }
//            setCoverageString(md5[1]);
//            setObstacleString(md5[2]);
//            return;
//        } else if (status.contains(FASTEST_FINISHED)) {
//            stopClock();
//        } else if (status.contains(EXPLORATION_FINISHED)) {
//            stopClock();
//        } else {
//            setCoverageString(DEFAULT_COVERAGE_STRING);
//            setObstacleString(DEFAULT_OBSTACLE_STRING);
//        }
        if (Constant.LOG) {
            Log.d(TAG, "Updated: " + receiveCommand.getStr());
        }
        mazeView.setCoordinate(x, y, dir);
//        mazeView.addObstacles(obstacles);
        mazeView.setGrid(grid);
        robotStatusView = (TextView) findViewById(R.id.robot_current_status);
        robotStatusView.setText(status);
    }

    private void setCoverageString(String s) {
        if (Constant.LOG) {
            Log.d(TAG, "setting coverage string: " + s);
        }
        coverageString = s;
    }

    private void setObstacleString(String s) {
        if (Constant.LOG) {
            Log.d(TAG, "setting obstacle string: " + s);
        }
        obstacleString = s;
    }

    private void resetClock() {
        timelapse = 0;
        clockTimer.setText(R.string.initial_clock);
    }

    private void stopClock() {
        clockTimer.setTextColor(Color.BLACK);
        timer.cancel();
        playButton.setIcon(R.mipmap.ic_play);
        isStarted = RobotStatus.STOPPED;
    }

    @Override
    public void onStop() {
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
