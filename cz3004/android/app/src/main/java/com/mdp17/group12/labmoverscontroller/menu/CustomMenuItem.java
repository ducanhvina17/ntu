package com.mdp17.group12.labmoverscontroller.menu;

import com.mdp17.group12.labmoverscontroller.ConfigurableActivity;
import com.mdp17.group12.labmoverscontroller.ExplorationMapActivity;
import com.mdp17.group12.labmoverscontroller.MainActivity;
import com.mdp17.group12.labmoverscontroller.PreInteractiveActivity;
import com.mdp17.group12.labmoverscontroller.R;
import com.mdp17.group12.labmoverscontroller.SendReceiveUI;

/**
 * Created by mrawesome on 4/2/17.
 */

public enum CustomMenuItem {
    INTERACTIVE(R.mipmap.ic_joystick, "Interactive", PreInteractiveActivity.class),
    LEADERBOARD(R.mipmap.ic_leaderboard, "Leaderboard", ExplorationMapActivity.class),
    SCAN_DEVICE(R.mipmap.ic_bluetooth, "Scan Devices", SendReceiveUI.class),
    CONFIGURABLES(R.mipmap.ic_edit, "Configurables", ConfigurableActivity.class);

    private String title;
    private int icon;
    private Class<? extends  MainActivity> intent;

    CustomMenuItem(int icon, String title, Class<? extends MainActivity> intent) {
        this.icon = icon;
        this.title = title;
        this.intent = intent;
    }

    public String getTitle() {
        return this.title;
    }
    
    public int getIcon() {
        return this.icon;
    }

    public Class getIntent() {
        return this.intent;
    }
}
