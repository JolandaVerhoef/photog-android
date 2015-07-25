package today.created.photog;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jolanda on 7/25/15.
 * Copyright (c) 2015 Blendle. All rights reserved.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private final Picasso mPicasso;
    private final ViewerFragment.OnAlbumSelectedListener mAlbumSelectedListener;

    public PhotoPagerAdapter(Picasso picasso, ViewerFragment.OnAlbumSelectedListener listener) {
        mPicasso = picasso;
        mAlbumSelectedListener = listener;
    }

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
                    mAlbumSelectedListener.onAlbumSelected(link);
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
