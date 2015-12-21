package de.sindzinski.helper;

/*
// Ad network-specific imports (AdMob).
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;

import android.app.Fragment;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
*/

import android.app.Fragment;

/**
 * Created by steffen on 01.10.13.
 */
public class AdFragment extends Fragment {
  /*  private AdView mAdView;
    private TextView mAdStatus;

    static AdFragment getInstance(int num) {
        AdFragment af = new AdFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        af.setArguments(args);

        return af;
    }

    *//**
     * When creating, retrieve this instance's number from its arguments.
     *//*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int mNum = args != null ? args.getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.ad, container, false);
        //mAdStatus = (TextView) v.findViewById(R.id.status);
        mAdView = (AdView) v.findViewById(R.id.ad);
        mAdView.setAdListener(new MyAdListener());

        AdRequest adRequest = new AdRequest();
        // adRequest.addKeyword("ad keywords");
        adRequest.addKeyword("linux");
        adRequest.addKeyword("linux server");
        adRequest.addKeyword("network");

        //adRequest.addTestDevice("802B891354C148BCD485A9BD5BE25D0D");
        // Ad network-specific mechanism to enable test mode.  Be sure to disable before
        // publishing your application.
        //adRequest.addTestDevice(TEST_DEVICE_ID);
        mAdView.loadAd(adRequest);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    // Receives callbacks on various events related to fetching ads.  In this sample,
    // the application displays a message on the screen.  A real application may,
    // for example, fill the ad with a banner promoting a feature.
    private class MyAdListener implements AdListener {

        @Override
        public void onDismissScreen(Ad ad) {}

        @Override
        public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {
            //mAdStatus.setText(R.string.error_receive_ad);
        }

        @Override
        public void onLeaveApplication(Ad ad) {}

        @Override
        public void onPresentScreen(Ad ad) {}

        @Override
        public void onReceiveAd(Ad ad) { }
    }*/
}
