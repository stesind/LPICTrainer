package de.sindzinski.lpictrainer;

import android.app.*;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import 	android.preference.PreferenceManager;
import android.widget.Toast;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;

//import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements MainFragment.OnTestListener {

    public final static String EXTRA_FILENAME = "de.sindzinski.lpictrainer.FILENAME";
    public final static String EXTRA_FROM = "de.sindzinski.lpictrainer.FROM";
    public final static String EXTRA_TO = "de.sindzinski.lpictrainer.TO";

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    protected static final int REQUEST_CODE_PREFERENCES = 2;

    protected EditText editText_file;
    protected SeekBar seekBar_from;
    protected SeekBar seekBar_to;
    protected TextView textView_to;
    protected TextView textView_from;
    private static final String TAG = "LPITrainer";

    private PreferenceManager chosenTheme;
    private boolean mTwoPane;

    public String fileName;
    public Integer from;
    public Integer to;
    public Integer max;
    private boolean isDarkTheme;
    private boolean showAd = true;

    public ArrayList<Entry> entries = null;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkTheme = sharedPref.getBoolean("pref_key_theme", this.getResources().getBoolean(R.bool.pref_key_dark_default));
        if (isDarkTheme) {
            this.setTheme(android.R.style.Theme_Holo);
        } else {
            this.setTheme(android.R.style.Theme_Holo_Light);
        }
        showAd = sharedPref.getBoolean("pref_key_ads", this.getResources().getBoolean(R.bool.pref_key_ads_default));

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

/*        SharedPreferences settings = this.getSharedPreferences("Settings", 0);
        fileName = settings.getString("fileName","").toString();
        from= settings.getInt("from",0);
        to = settings.getInt("to",0);
        max = settings.getInt("max",0);
        //onTest(from, to, fileName);*/
        fileName = sharedPref.getString("fileName","").toString();
        from= sharedPref.getInt("from",0);
        to = sharedPref.getInt("to",0);
        max = sharedPref.getInt("max",0);
        //onTest(from, to, fileName);


        MainFragment mainFragment = new MainFragment();
        mainFragment = MainFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        //transaction.addToBackStack("main");
        transaction.replace(R.id.container, mainFragment);
        //transaction.addToBackStack("main");

        // Commit the transaction
        transaction.commit();
        //testFragment.update();

        if (showAd) {
            //ad
            AdFragment adFragment = new AdFragment();
            mainFragment = MainFragment.newInstance();
            FragmentTransaction adTransaction = getFragmentManager().beginTransaction();
            adTransaction.replace(R.id.ad_container, adFragment);
            adTransaction.commit();
        }

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
/*        if (findViewById(R.id.container_two_pane) != null) {
            mTwoPane = true;
            onTest(from, to, fileName);
        } else {
            mTwoPane = false;
        }*/

        //show app rater dialog
        AppRater.appLaunched(this);
        Trial.checkTrial(this, true);
    }


    public void onTest(int from, int to, String fileName, int max) {
        // Create new fragment and transact();

        safeSettings(from,to, fileName, max);

        TestFragment testFragment = (TestFragment)
                getFragmentManager().findFragmentByTag("test");

        if (testFragment != null) {
            //just update the fragment
            //testFragment.update();)) {
            testFragment = TestFragment.newInstance(from, to, fileName);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
        } else {
            //transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_righ             t);
            //        transaction.setCustomAnimatio            ns(
            //                R.anim.card_flip_right_in, R.anim.card_flip_right_o            ut,
            //                R.anim.card_flip_left_in, R.anim.card_flip_left_o

            // Create fragment
            testFragment = new TestFragment();
            //Arguments can go by bundle or by instance
//            Bundle args = new Bundle();
//            args.putInt(TestFragment.ARG_POSITION, position);
//            testFragment.setArguments(args);
            testFragment = TestFragment.newInstance(from, to, fileName);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
/*            if (findViewById(R.id.container_two_pane) != null) {
                transaction.replace(R.id.container_two_pane, testFragment);
            } else {
                transaction.replace(R.id.container, testFragment);
            }*/
            transaction.replace(R.id.container, testFragment);
            transaction.addToBackStack("test");
            // Commit the transaction
            transaction.commit();
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
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                // Handle Settings
                //showSettingsActivity();
                showSettingsFragment();
                return true;
            case R.id.about:
                //showAboutDialog();
                HelpUtils.showAbout(this);
                //showAboutDialogFragment();
                return true;
            case R.id.menu_legalnotices:
                //showLegalNoticeDialog();
                //showLegalNoticeDialogFragment();
                HelpUtils.showOpenSourceLicenses(this);
                return true;
            case R.id.help:
                //startActivity(new Intent(this, Help.class));
                //showHelpDialogFragment();
                HelpUtils.showHelp(this);
                return true;
            case R.id.eula:
                //startActivity(new Intent(this, Help.class));
                HelpUtils.showEula(this);
                return true;
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

/*    public void showAboutDialogFragment() {
        DialogFragment newFragment = new AboutDialogFragment();
        newFragment.show(getFragmentManager(), "about");
    }

    public void showLegalNoticeDialogFragment() {

        DialogFragment newFragment = new LegalNoticeDialogFragment();
        newFragment.show(getFragmentManager(), "legalNotice");
    }

    public void showHelpDialogFragment() {
        DialogFragment newFragment = new HelpDialogFragment();
        newFragment.show(getFragmentManager(), "help");
    }*/



    /*//not used anymore
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
    }   */

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

    public void safeSettings(int from, int to, String fileName, int max) {

        //editText_file = (EditText) view.findViewById(R.id.editText_file);

        // during onDestroy the fragment is detached from the activity so getActivity returns null!!!!!
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences settings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fileName", fileName.toString() );
        editor.putInt("max", max);
        editor.putInt("from", from);
        editor.putInt("to", to);
        // Commit the edits!
        editor.commit();

    }

    // We're being destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();

/*        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) mHelper.dispose();
        mHelper = null;*/
    }

}
