package today.created.photog;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import icepick.Icepick;
import icepick.Icicle;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This fragment can show one album using a viewpager.
 * The url for this album is passed through via the fragment's arguments
 */
public class AlbumFragment extends Fragment implements PhotogView.OnFlingListener,
        ViewPager.OnPageChangeListener {

    private ImageButton mViewOrientationChooser;

    public interface OnAlbumSelectedListener {
        void onAlbumSelected(String path);
    }

    private View bottomFeaturesView;
    private String mHost;
    private String mPath;

    @Icicle
    ArrayList<PhotoItem> mPhotoItems = new ArrayList<>();

    @Icicle
    int currentPage = 0;

    private PhotoPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mHost = bundle.getString("host");
        mPath = bundle.getString("path");
        return inflater.inflate(R.layout.fragment_viewer, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        mAdapter = new PhotoPagerAdapter(Picasso.with(getActivity()),
                (OnAlbumSelectedListener) getParentFragment());

        ViewPager mViewPager = (HackyViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);

        if(mPhotoItems.size() > 0) {
            mAdapter.setPhotoItems(mPhotoItems);
            mViewPager.setCurrentItem(currentPage, false);
        } else {
            loadPhotos();
        }

        mViewPager.addOnPageChangeListener(this);
        ((PhotogView) rootView).setOnFlingUpListener(this);

        bottomFeaturesView = rootView.findViewById(R.id.bottom_features);
        mViewOrientationChooser = (ImageButton) bottomFeaturesView.findViewById(R.id.orientation_chooser);
        mViewOrientationChooser.setImageResource(getOrientationDrawable(getActivity().getRequestedOrientation()));
        mViewOrientationChooser.setOnClickListener(view -> openOrientationDialog());
        bottomFeaturesView.findViewById(R.id.location_chooser).setOnClickListener(view1 -> openLocationDialog());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onFlingUp() {
        showBottomFeatures();
    }

    @Override
    public void onFlingDown() {
        hideBottomFeatures();
    }

    private void loadPhotos() {
        String url = mHost + mPath;
        mPhotoItems.clear();
        mAdapter.setPhotoItems(mPhotoItems);
        retrieveHtmlPageObservable(url)
                .map(d -> d.select("#album img"))
                .flatMap(elements -> Observable.from(elements.subList(0, elements.size())))
                .map(element -> PhotoItem.create(element.attr("src"), mHost, mPath))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPhotoItems::add,
                        throwable -> mAdapter.setPhotoItems(new ArrayList<>()),
                        () -> mAdapter.setPhotoItems(mPhotoItems));
    }

    private Observable<Document> retrieveHtmlPageObservable(String url) {
        return Observable.create(observer -> {
                    try {
                        observer.onNext(retrieveHtmlPage(url));
                    } catch (IOException e) {
                        observer.onError(e);
                    }
                    observer.onCompleted();
                }
        );
    }
    private Document retrieveHtmlPage(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return Jsoup.parse(response.body().string());
    }

    private void openLocationDialog() {
        EnterLocationDialogFragment.newInstance(mPath).show(getFragmentManager(), "dialog");
    }

    private void openOrientationDialog() {
        int currentOrientation = getActivity().getRequestedOrientation();
        OrientationDialogFragment dialogFragment = OrientationDialogFragment.newInstance(currentOrientation);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "orientation_dialog");
    }

    private void showBottomFeatures() {
        bottomFeaturesView.setVisibility(View.VISIBLE);
    }

    private void hideBottomFeatures() {
        bottomFeaturesView.setVisibility(View.GONE);
    }

    public void onOrientationSelected(int choice) {
        int orientation;
        switch(choice) {
            case 0: orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; break;
            case 1: orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; break;
            default: orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR; break;
        }
        getActivity().setRequestedOrientation(orientation);
        mViewOrientationChooser.setImageResource(getOrientationDrawable(orientation));
    }

    private int getOrientationDrawable(int requestedOrientation) {
        switch (requestedOrientation) {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT: return R.drawable.ic_action_screen_lock_portrait;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE: return R.drawable.ic_action_screen_lock_landscape;
            default: return R.drawable.ic_action_screen_rotation;
        }
    }
}
