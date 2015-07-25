package today.created.photog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(url);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.open_url)
                .setView(input)
                .setPositiveButton(R.string.open,
                        (dialog, whichButton) -> {
                            ((PhotogFragment) this.getParentFragment()).onAlbumSelected(
                                    input.getText().toString());
                        }
                )
                .create();
    }
}