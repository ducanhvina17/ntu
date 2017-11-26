package com.mdp17.group12.labmoverscontroller.enumType;

/**
 * Created by mrawesome on 7/3/17.
 */

public enum BluetoothState {
    NONE(0), LISTENING(1), CONNECTING(2), CONNECTED(3);

    private int code;

    BluetoothState(int code) {
        this.code = code;
    }

    public static BluetoothState getEnum(int code) {
        switch (code) {
            case 1:
                return LISTENING;
            case 2:
                return CONNECTING;
            case 3:
                return CONNECTED;
            default:
                return NONE;
        }
    }

    public int getCode() {
        return this.code;
    }
}
