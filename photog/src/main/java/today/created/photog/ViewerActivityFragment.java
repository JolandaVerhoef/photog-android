package today.created.photog;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import icepick.Icepick;
import icepick.Icicle;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewerActivityFragment extends Fragment {
    public interface OnAlbumSelectedListener {
        void onAlbumSelected(String url);
    }

    private Activity mActivity;

    private String  mBaseUrl;
    private Picasso mPicasso;
    @Icicle
    ArrayList<PhotoItem> mPhotoItems = new ArrayList<>();
    @Icicle
    int                  currentPage = 0;
    private SamplePagerAdapter mAdapter;

    public ViewerActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

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
        mBaseUrl = bundle.getString("baseUrl");
        return inflater.inflate(R.layout.fragment_viewer, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        mAdapter = new SamplePagerAdapter();
        ViewPager mViewPager = (HackyViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);

        if(mPhotoItems.size() > 0) {
            mAdapter.setPhotoItems(mPhotoItems);
            mViewPager.setCurrentItem(currentPage, false);
        } else {
            loadPhotos(mBaseUrl);
        }
        mViewPager.setCurrentItem(currentPage);
        mPicasso = Picasso.with(getActivity());


    }

    private void loadPhotos(String url) {
        mPhotoItems.clear();
        mAdapter.setPhotoItems(mPhotoItems);
        retrieveHtmlPageObservable(url)
                .map(d -> d.select("#album img"))
                .flatMap(elements -> Observable.from(elements.subList(0, elements.size())))
                .map(element -> PhotoItem.create(element.attr("src"), url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPhotoItems::add,
                        Throwable::printStackTrace,
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

    class SamplePagerAdapter extends PagerAdapter {

        private List<PhotoItem> mPhotoItems = new ArrayList<>();

        @Override
        public int getCount() {
            return mPhotoItems.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final PhotoItem photoItem = mPhotoItems.get(position);
            PhotoView photoView = new PhotoView(container.getContext());
            mPicasso.load(photoItem.baseUrl() + photoItem.src())
                .into(photoView);

            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    if(photoItem.src().endsWith("thumbnails/all.jpg")) {
                        String link = photoItem.baseUrl() +
                                photoItem.src().replace("thumbnails/all.jpg", "");

                        ((OnAlbumSelectedListener) mActivity).onAlbumSelected(link);
                    }
                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public int getItemPosition (Object object) { return POSITION_NONE; }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setPhotoItems(List<PhotoItem> photoItems) {
            mPhotoItems = new ArrayList<>();
            for(PhotoItem photoItem : photoItems) {
                mPhotoItems.add(photoItem);
            }
            notifyDataSetChanged();
        }
    }

}
