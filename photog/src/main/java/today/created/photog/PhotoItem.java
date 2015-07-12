package today.created.photog;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

/**
 * Created by jolandaverhoef on 12/07/15.
 */
@AutoParcel
public abstract class PhotoItem implements Parcelable {
    abstract String src();
    abstract String baseUrl();

    static PhotoItem create(String src, String baseUrl) {
        return new AutoParcel_PhotoItem(src, baseUrl);
    }
}