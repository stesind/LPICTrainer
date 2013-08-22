package de.sindzinski.lpitrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.ActionBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;

//import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements MainFragment.OnTestListener {

    public final static String EXTRA_FILENAME = "de.sindzinski.lpitrainer.FILENAME";
    public final static String EXTRA_FROM = "de.sindzinski.lpitrainer.FROM";
    public final static String EXTRA_TO = "de.sindzinski.lpitrainer.TO";

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    protected static final int REQUEST_CODE_PREFERENCES = 2;

    protected EditText editText_file;
    protected SeekBar seekBar_from;
    protected SeekBar seekBar_to;
    protected TextView textView_to;
    protected TextView textView_from;
    private static final String TAG = "LPITrainer";

    private boolean mTwoPane;


    public ArrayList<Entry> entries = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        startMainFragment();
        //determine if it is two pane or not, if so, fill in an default test fragment
        if (findViewById(R.id.container_two_pane) != null) {
            mTwoPane = true;
            SharedPreferences settings = this.getSharedPreferences("Settings", 0);

            String fileName = settings.getString("fileName","").toString();
            Integer from= settings.getInt("from",0);
            Integer to = settings.getInt("to",0);
            onTest(from, to, fileName);
        }
        // TODO: If exposing deep links into your app, handle intents here.
    }

    public void onTest(int from, int to, String fileName) {
        // Create new fragment and transaction
        TestFragment testFragment = new TestFragment();
        //testFragment = (TestFragment) getFragmentManager().findFragmentById(R.id.frameLayout);
        //if (testFragment == null || ! testFragment.isInLayout()) {
        testFragment = TestFragment.newInstance(from, to, fileName);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        //transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        if (mTwoPane) {
            transaction.addToBackStack("test");
            transaction.replace(R.id.container_two_pane, testFragment);
        } else {
            transaction.addToBackStack("test");
            transaction.replace(R.id.container, testFragment);
        }

        // Commit the transaction
        transaction.commit();
        //} else {
        //testFragment.update();
        //}
    }

    public void startMainFragment() {

        // Create new fragment and transaction
        MainFragment mainFragment = new MainFragment();
        mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("main");
        if (mainFragment == null || !mainFragment.isInLayout()) {
            mainFragment = MainFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.container, mainFragment);
            transaction.addToBackStack("main");

            // Commit the transaction
            transaction.commit();
        } else {
            //testFragment.update();
        }
    }

    //not neccacary because fragments are on the backstack
    @Override
    public void onBackPressed() {
/*        final Myfragment fragment = (Myfragment) getSupportFragmentManager().findFragmentByTag("test");

        if (fragment.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            super.onBackPressed();
        }*/
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // Handle Settings
                //showSettingsActivity();
                showSettingsFragment();
                return true;
            case R.id.about:
                //showAboutDialog();
                showAboutDialogFragment();
                return true;
            case R.id.menu_legalnotices:
                //showLegalNoticeDialog();
                showLegalNoticeDialogFragment();
                return true;
            /*case R.id.help:
                //startActivity(new Intent(this, Help.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSettingsFragment() {
        // Display the fragment as the main content.
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new SettingsFragment());
        transaction.addToBackStack("settings");
        transaction.commit();

    }

    public void showAboutDialogFragment() {
        DialogFragment newFragment = new AboutDialogFragment();
        newFragment.show(getFragmentManager(), "about");
    }

    public void showLegalNoticeDialogFragment() {
        DialogFragment newFragment = new LegalNoticeDialogFragment();
        newFragment.show(getFragmentManager(), "legalNotice");
    }

    //not used anymore
    public void showLegalNoticeDialog() {
        String licenseInfo = null;
        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("License");
            licenseInfo = convertStreamToString(is);
        } catch (IOException e) {
            Log.e(TAG, "Error reading file: " + e);
        }
        AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
        LicenseDialog.setTitle("Legal Notices");
        LicenseDialog.setMessage(licenseInfo);
        LicenseDialog.show();
    }

    //not used anymore
    public void showAboutDialog() {
        //startActivity(new Intent(this, About.class));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("About");

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.copyright)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * This is called after the file manager finished.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //editText_file.setText("");

        switch (requestCode) {
            case REQUEST_CODE_PREFERENCES:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the preferences

                }
                break;

        }
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        is.close();

        return sb.toString();
    }


}
