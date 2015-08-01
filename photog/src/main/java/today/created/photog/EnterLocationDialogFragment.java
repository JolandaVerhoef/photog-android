package today.created.photog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Shows a dialog in which you can enter a location to browse to.
 */
public class EnterLocationDialogFragment extends DialogFragment {

    public static EnterLocationDialogFragment newInstance(String currentUrl) {
        EnterLocationDialogFragment frag = new EnterLocationDialogFragment();
        Bundle args = new Bundle();
        args.putString("url", currentUrl);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String url = getArguments().getString("url");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_location, null);
        final EditText input = (EditText) view.findViewById(R.id.location_input);
        input.setText(url);
        input.setSelection(input.getText().length());
        input.requestFocus();
        final PhotogFragment parentFragment = (PhotogFragment) getParentFragment();

        Dialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.enter_location)
                .setView(view)
                .setPositiveButton(R.string.go, (dialog, whichButton) ->
                            parentFragment.onAlbumSelected(input.getText().toString()))
                .setCancelable(true)
                .create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return alertDialog;
    }
}