package com.mdp17.group12.labmoverscontroller.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.mdp17.group12.labmoverscontroller.R;
import com.mdp17.group12.labmoverscontroller.behaviour.ResetDialogWrapper;
import com.mdp17.group12.labmoverscontroller.enumType.ResetCode;

/**
 * Created by mrawesome on 7/3/17.
 */

public class ResetDialogFragment extends android.support.v4.app.DialogFragment {

    public static final String TITLE = "title";
    public static final String ACTION = "action";

    public static android.support.v4.app.DialogFragment newInstance(int title, ResetCode action) {
        android.support.v4.app.DialogFragment frag = new ResetDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putInt(ACTION, action.getCode());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(TITLE);
        final int action = getArguments().getInt(ACTION);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.confirm_text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                ((ResetDialogWrapper) getActivity())
                                        .doPositiveClick(ResetCode.getEnum(action));
                            }
                        })
                .setNegativeButton(R.string.cancel_text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                ((ResetDialogWrapper) getActivity())
                                        .doNegativeClick();
                            }
                        }).create();
    }
}
