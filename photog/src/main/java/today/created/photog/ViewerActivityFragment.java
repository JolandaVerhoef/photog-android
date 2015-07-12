package today.created.photog;

import android.app.Activity;
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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ViewerActivityFragment extends Fragment {
    private String mBaseUrl;
    private Picasso mPicasso;

    public ViewerActivityFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        ViewPager mViewPager = (HackyViewPager) rootView.findViewById(R.id.view_pager);
        SamplePagerAdapter mAdapter = new SamplePagerAdapter();
        mViewPager.setAdapter(mAdapter);

        mPicasso = Picasso.with(getActivity());

        retrieveHtmlPageObservable(mBaseUrl)
                .map(d -> d.select("#album img"))
                .flatMap(elements -> Observable.from(elements.subList(0, elements.size())))
                .map(element -> new PhotoItem(element.attr("src"), mBaseUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter::addPhotoItem,
                        Throwable::printStackTrace,
                        mAdapter::notifyDataSetChanged);
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

        private final List<PhotoItem> mPhotoItems = new ArrayList<>();

        @Override
        public int getCount() {
            return mPhotoItems.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoItem photoItem = mPhotoItems.get(position);
            PhotoView photoView = new PhotoView(container.getContext());
            mPicasso.load(photoItem.getBaseUrl() + photoItem.getSrc())
                .placeholder(R.drawable.placeholder)
                .into(photoView);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void addPhotoItem(PhotoItem photoItem) {
            mPhotoItems.add(photoItem);
        }

    }

}
