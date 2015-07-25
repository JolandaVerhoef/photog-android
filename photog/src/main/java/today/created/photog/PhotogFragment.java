package today.created.photog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PhotogFragment extends Fragment
        implements ViewerFragment.OnAlbumSelectedListener {

    View bottomFeaturesView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bottomFeaturesView = view.findViewById(R.id.bottom_features);
        ((PhotogView) view).setOnFlingUpListener(new PhotogView.OnFlingListener() {
            @Override
            public void onFlingUp() {
                showBottomFeatures();
            }

            @Override
            public void onFlingDown() {
                hideBottomFeatures();
            }
        });
        onAlbumSelected(getArguments().getString("baseUrl"));
    }

    private void showBottomFeatures() {
        bottomFeaturesView.setVisibility(View.VISIBLE);
    }

    private void hideBottomFeatures() {
        bottomFeaturesView.setVisibility(View.GONE);
    }

    @Override
    public void onAlbumSelected(String url) {
        Fragment fragment = new ViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("baseUrl", url);
        fragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.photo_container, fragment).commit();
    }
}
