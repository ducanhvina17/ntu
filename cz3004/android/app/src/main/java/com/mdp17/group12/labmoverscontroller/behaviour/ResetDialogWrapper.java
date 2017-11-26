package com.mdp17.group12.labmoverscontroller.behaviour;

import com.mdp17.group12.labmoverscontroller.enumType.ResetCode;

/**
 * Created by mrawesome on 7/3/17.
 */

public interface ResetDialogWrapper {
    void doPositiveClick(ResetCode action);
    void doNegativeClick();
}
