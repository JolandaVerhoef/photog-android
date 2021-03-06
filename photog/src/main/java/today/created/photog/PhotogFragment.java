package today.created.photog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Main fragment of this library. This fragment takes care of the stack of albums being shown
 * to the user.
 */
public class PhotogFragment extends Fragment
        implements AlbumFragment.OnAlbumSelectedListener {

    private String host;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setRequestedOrientation(activity.getSharedPreferences("photog", Context.MODE_PRIVATE).getInt("orientation", ActivityInfo.SCREEN_ORIENTATION_SENSOR));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        host = getArguments().getString("baseUrl");
        if(getChildFragmentManager().findFragmentByTag("baseFragment") == null) {
            Fragment fragment = getAlbumFragment("/");
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.photog_container, fragment, "baseFragment").commit();
        }
    }

    @Override
    public void onAlbumSelected(String path) {
        Fragment fragment = getAlbumFragment(path);
        getChildFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.photog_container, fragment).commit();
    }

    private Fragment getAlbumFragment(String path) {
        path = sanitizePath(path);
        Fragment fragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("host", host);
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String sanitizePath(String path) {
        String result = path;
        if(!result.startsWith("/")) result = '/' + result;
        if(!result.endsWith("/")) result = result + '/';
        return result;
    }

    public boolean onBackPressed() {
        return getChildFragmentManager().popBackStackImmediate();
    }
}
