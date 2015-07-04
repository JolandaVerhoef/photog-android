package today.created.photog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ViewerActivityFragment extends Fragment {

    private String mBaseUrl;

    public ViewerActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mBaseUrl = bundle.getString("baseUrl");
        return inflater.inflate(R.layout.fragment_viewer, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        TextView textView = (TextView) rootView.findViewById(R.id.sample_text);
        textView.setText(mBaseUrl);
    }
}
