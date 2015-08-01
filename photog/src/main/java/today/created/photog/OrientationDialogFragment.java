package today.created.photog;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

/**
 * Created by jolanda on 8/1/15.
 * Copyright (c) 2015 Blendle. All rights reserved.
 */
public class OrientationDialogFragment extends DialogFragment {
    private int choice;

    public OrientationDialogFragment() {
    }

    public static OrientationDialogFragment newInstance(int currentOrientation) {
        int choice;
        switch(currentOrientation) {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT: choice = 0; break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE: choice = 1; break;
            default: choice = 2; break;
        }
        OrientationDialogFragment frag = new OrientationDialogFragment();
        Bundle args = new Bundle();
        args.putInt("choice", choice);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlbumFragment targetFragment = (AlbumFragment) getTargetFragment();
        int currentChoice = getArguments().getInt("choice");

        Dialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.enter_location)
                .setSingleChoiceItems(getActivity().getResources().getStringArray(R.array.orientation_options),
                        currentChoice, (dialogInterface, i) -> {
                    choice = i;
                })
                .setPositiveButton(R.string.go, (dialog, whichButton) -> {
                    targetFragment.onOrientationSelected(getChoice());
                    this.dismiss();
                })
                .setCancelable(true)
                .create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return alertDialog;
    }

    public int getChoice() {
        return choice;
    }
}
