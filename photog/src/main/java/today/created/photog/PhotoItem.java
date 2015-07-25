package today.created.photog;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

/**
 * Created by jolandaverhoef on 12/07/15.
 */
@AutoParcel
public abstract class PhotoItem implements Parcelable {
    abstract String src();
    abstract String host();
    abstract String path();

    static PhotoItem create(String src, String host, String path) {
        return new AutoParcel_PhotoItem(src, host, path);
    }
}