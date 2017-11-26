package com.mdp17.group12.labmoverscontroller.enumType;

/**
 * Created by mrawesome on 7/3/17.
 */

public enum ResetCode {
    OBSTACLES_RESET_CODE(0), ROBOT_RESET_CODE(1);

    private int code;

    ResetCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ResetCode getEnum(int code) {
        switch (code) {
            case 0:
                return OBSTACLES_RESET_CODE;
            default:
                return ROBOT_RESET_CODE;
        }
    }
}
