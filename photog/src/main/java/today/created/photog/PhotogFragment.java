package today.created.photog;

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
        Fragment fragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("host", host);
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    public boolean onBackPressed() {
        return getChildFragmentManager().popBackStackImmediate();
    }
}
