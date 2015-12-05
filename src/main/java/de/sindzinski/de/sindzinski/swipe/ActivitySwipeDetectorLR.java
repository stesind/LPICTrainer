package de.sindzinski.de.sindzinski.swipe;

/**
 * Created by steffen on 13.08.13.
 */
/*
only detects left and right swipe
has built-in relative swipe metrics

needs class SwipeInterface

implemented by in the action:

        ActivitySwipeDetectorLR swipe = new ActivitySwipeDetectorLR(this,new SwipeInterfaceLR() {
            @Override
            public void onLeftToRight(View v) {
                Toast.makeText(TestActivity.this, "left", Toast.LENGTH_SHORT).show();
                prevQuestion();
            }

            @Override
            public void onRightToLeft(View v) {
                Toast.makeText(TestActivity.this, "right", Toast.LENGTH_SHORT).show();

                nextQuestion();
            }
        });
        LinearLayout swipe_layout = (LinearLayout) findViewById(R.id.linearLayout);
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
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import de.sindzinski.helper.Logger;

public class ActivitySwipeDetectorLR implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private SwipeInterfaceLR activity;
    private float downX, downY;
    private long timeDown;
    private final float MIN_DISTANCE;
    private final int VELOCITY;
    private final float MAX_OFF_PATH;

    public ActivitySwipeDetectorLR(Context context, SwipeInterfaceLR activity){
        this.activity = activity;
        final ViewConfiguration vc = ViewConfiguration.get(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        MIN_DISTANCE = vc.getScaledPagingTouchSlop() * dm.density;
        VELOCITY = vc.getScaledMinimumFlingVelocity();
        MAX_OFF_PATH = MIN_DISTANCE * 2;
    }

    public void onRightToLeftSwipe(View v){
        Logger.d(logTag, "RightToLeftSwipe!");
        activity.onRightToLeft(v);
    }

    public void onLeftToRightSwipe(View v){
        Logger.d(logTag, "LeftToRightSwipe!");
        activity.onLeftToRight(v);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                Log.d("onTouch", "ACTION_DOWN");
                timeDown = System.currentTimeMillis();
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                Log.d("onTouch", "ACTION_UP");
                long timeUp = System.currentTimeMillis();
                float upX = event.getX();
                float upY = event.getY();

                float deltaX = downX - upX;
                float absDeltaX = Math.abs(deltaX);
                float deltaY = downY - upY;
                float absDeltaY = Math.abs(deltaY);

                long time = timeUp - timeDown;

                if (absDeltaY > MAX_OFF_PATH) {
                    Logger.d(logTag, String.format("absDeltaY=%.2f, MAX_OFF_PATH=%.2f", absDeltaY, MAX_OFF_PATH));
                    return v.performClick();
                }

                final long M_SEC = 1000;
                if (absDeltaX > MIN_DISTANCE && absDeltaX > time * VELOCITY / M_SEC) {
                    if(deltaX < 0) { this.onLeftToRightSwipe(v); return true; }
                    if(deltaX > 0) { this.onRightToLeftSwipe(v); return true; }
                } else {
                    Logger.d(logTag, String.format("absDeltaX=%.2f, MIN_DISTANCE=%.2f, absDeltaX > MIN_DISTANCE=%b", absDeltaX, MIN_DISTANCE, (absDeltaX > MIN_DISTANCE)));
                    Logger.d(logTag, String.format("absDeltaX=%.2f, time=%d, VELOCITY=%d, time*VELOCITY/M_SEC=%d, absDeltaX > time * VELOCITY / M_SEC=%b", absDeltaX, time, VELOCITY, time * VELOCITY / M_SEC, (absDeltaX > time * VELOCITY / M_SEC)));
                }

            }
        }
        return false;
    }

}
