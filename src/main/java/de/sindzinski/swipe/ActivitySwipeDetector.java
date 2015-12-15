package de.sindzinski.swipe;

/**
 * Created by steffen on 13.08.13.
 */
/*
needs class SwipeInterface

implemented by in the action:

ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
LinearLayout swipe_layout = (LinearLayout) findViewById(R.id.swipe_layout);
swipe_layout.setOnTouchListener(swipe);

@Override
public void left2right(View v) {
    switch(v.getId()){
        case R.id.swipe_layout:
            // do your stuff here
        break;
    }
}

 */

import android.view.MotionEvent;
import android.view.View;

import de.sindzinski.helper.Logger;

public class ActivitySwipeDetector implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private SwipeInterface activity;
    static final int MIN_DISTANCE = 100;
    private float downX;
    private float downY;

    /*
    //instead using relative metrics:
    DisplayMetrics dm = getResources().getDisplayMetrics();

    int REL_SWIPE_MIN_DISTANCE = (int)(SWIPE_MIN_DISTANCE * dm.densityDpi / 160.0f);
    int REL_SWIPE_MAX_OFF_PATH = (int)(SWIPE_MAX_OFF_PATH * dm.densityDpi / 160.0f);
    int REL_SWIPE_THRESHOLD_VELOCITY = (int)(SWIPE_THRESHOLD_VELOCITY * dm.densityDpi / 160.0f);
     */

    public ActivitySwipeDetector(SwipeInterface activity){
        this.activity = activity;
    }

    public void onRightToLeftSwipe(View v){
        Logger.d(logTag, "RightToLeftSwipe!");
        activity.right2left(v);
    }

    public void onLeftToRightSwipe(View v){
        Logger.d(logTag, "LeftToRightSwipe!");
        activity.left2right(v);
    }

    public void onTopToBottomSwipe(View v){
        Logger.d(logTag, "onTopToBottomSwipe!");
        activity.top2bottom(v);
    }

    public void onBottomToTopSwipe(View v){
        Logger.d(logTag, "onBottomToTopSwipe!");
        activity.bottom2top(v);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0) { this.onLeftToRightSwipe(v); return true; }
                    if(deltaX > 0) { this.onRightToLeftSwipe(v); return true; }
                }
                else {
                    Logger.d(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }

                // swipe vertical?
                if(Math.abs(deltaY) > MIN_DISTANCE){
                    // top or down
                    if(deltaY < 0) { this.onTopToBottomSwipe(v); return true; }
                    if(deltaY > 0) { this.onBottomToTopSwipe(v); return true; }
                }
                else {
                    Logger.d(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    v.performClick();
                }
            }
        }
        return false;
    }

}
