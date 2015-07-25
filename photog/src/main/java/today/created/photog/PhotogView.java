package today.created.photog;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Extension of FrameLayout that intercepts touch events.
 */
public class PhotogView extends FrameLayout {
    public interface OnFlingListener {
        void onFlingUp();
        void onFlingDown();
    }

    private GestureDetectorCompat mDetector;
    private OnFlingListener mFlingListener;

    public PhotogView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mDetector = new GestureDetectorCompat(context, new FlingListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return false;
    }

    public void setOnFlingUpListener(OnFlingListener onFlingListener) {
        mFlingListener = onFlingListener;
    }

    private class FlingListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if(mFlingListener != null) {
                if(event1.getY() - event2.getY() > 0) {
                    mFlingListener.onFlingUp();
                } else {
                    mFlingListener.onFlingDown();
                }
            }
            return true;
        }
    }
}
