package de.sindzinski.lpictrainer;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.view.ViewConfiguration;

/**
 * Created by steffen on 10.10.13.
 */
public class ContainerLayout extends RelativeLayout {

    private static final String TAG="ContainerLayout";

    public ContainerLayout(Context context) {
        super(context);

    }
    public ContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getActionMasked();
        float lastX = 0;
        float currentX = 0;
        float xDiff = 0;
        float lastY = 0;
        float currentY = 0;
        float yDiff = 0;
        int lastAction;
        ViewConfiguration vc = ViewConfiguration.get(ContainerLayout.this.getContext());
        int mTouchSlop = vc.getScaledTouchSlop();
        Boolean mIsScrolling;

        // Always handle the case of the touch gesture being complete.
/*
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll.
            return false; // Do not intercept touch event, let the child handle it
        }
*/
        /* ACTION_DOWN must return false to let react on buttons and so

         */
        lastAction = action;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent.ACTION_DOWN");
                return false;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent.ACTION_MOVE");
                return true;
                /*if (ev.getHistorySize() > 0) {

                    lastX = ev.getHistoricalX(0);
                    currentX = ev.getX();
                    if (lastX > currentX) {
                        xDiff = lastX - currentX;
                    } else {
                        xDiff = currentX -lastX;
                    }

                    lastY = ev.getHistoricalY(0);
                    currentY = ev.getY();
                    if (lastY > currentY) {
                        yDiff = lastY - currentY;
                    } else {
                        yDiff = currentY -lastY;
                    }
                    Log.i(TAG, "onInterceptTouchEvent.ACTION_MOVE X - " + xDiff);
                    Log.i(TAG, "onInterceptTouchEvent.ACTION_MOVE Y - " + yDiff);
                }

                // Touch slop should be calculated using ViewConfiguration
                // constants.
                if (xDiff > mTouchSlop) {
                    // Start scrolling!
                    mIsScrolling = true;
                    Log.i(TAG, "onInterceptTouchEvent.ACTION_MOVE - X scrolling" + xDiff);
                    return true;
                }
                if (yDiff > mTouchSlop) {
                    // Start scrolling!
                    mIsScrolling = true;
                    Log.i(TAG, "onInterceptTouchEvent.ACTION_MOVE - Y scrolling" + yDiff);
                    return true;
                }
                break;*/
            case MotionEvent.ACTION_CANCEL:
                mIsScrolling = false;
                Log.i(TAG, "onInterceptTouchEvent.ACTION_CANCEL");
                return false;
            case MotionEvent.ACTION_UP:
                mIsScrolling = false;
                Log.i(TAG, "onInterceptTouchEvent.ACTION_UP");
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    Log.i(TAG, "onInterceptTouchEvent.ACTION_UP");
                    return false;
                }

                return false;
        }
        //return super.onInterceptTouchEvent(ev);
        return true;
    }



/*
    ViewConfiguration vc = ViewConfiguration.get(ContainerLayout.this.getContext());
    int mTouchSlop = vc.getScaledTouchSlop();
    Boolean mIsScrolling;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

*/
/*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         *//*


        float lastX = 0;
        float currentX = 0;
        float xDiff = 0;
        float lastY = 0;
        float currentY = 0;
        float yDiff = 0;

        final int action = MotionEventCompat.getActionMasked(ev);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP ) {
            // Release the scroll.
            mIsScrolling = false;
            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    // We're currently scrolling, so yes, intercept the
                    // touch event!
                    return true;
                }

                // If the user has dragged her finger horizontally more than
                // the touch slop, start the scroll

                // left as an exercise for the reader
                if (ev.getHistorySize() > 0) {

                    lastX = ev.getHistoricalX(0);
                    currentX = ev.getX();
                    xDiff = lastX - currentX;

                    lastY = ev.getHistoricalY(0);
                    currentY = ev.getY();
                    yDiff = lastX - currentX;

                }

                // Touch slop should be calculated using ViewConfiguration
                // constants.
                if (xDiff > mTouchSlop) {
                    // Start scrolling!
                    mIsScrolling = true;
                    return true;
                }
                if (yDiff > mTouchSlop) {
                    // Start scrolling!
                    mIsScrolling = true;
                    return true;
                }
                break;
            }

        }

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }
*/


}

