package today.created.photog;

/**
 * Created by jolandaverhoef on 12/07/15.
 */
public class PhotoItem {
    private final String src;
    private final String baseUrl;

    public PhotoItem(String src, String baseUrl) {
        this.src = src;
        this.baseUrl = baseUrl;
    }

    public String getSrc() {
        return src;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
