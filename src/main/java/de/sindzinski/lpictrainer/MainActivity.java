package de.sindzinski.lpictrainer;

import android.app.*;
import android.app.FragmentTransaction;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import 	android.preference.PreferenceManager;
import android.widget.SimpleCursorAdapter;

//import de.sindzinski.helper.AppRater;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import de.sindzinski.lpictrainer.data.TrainerContract.QuestionEntry;
import de.sindzinski.lpictrainer.data.TrainerContract;
import de.sindzinski.util.HelpUtils;
import de.sindzinski.util.Logger;
import de.sindzinski.lpictrainer.data.QuestionContentProvider;

//import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements MainFragment.OnTestListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    public final static String EXTRA_FILENAME = "de.sindzinski.lpictrainer.FILENAME";
    public final static String EXTRA_FROM = "de.sindzinski.lpictrainer.FROM";
    public final static String EXTRA_TO = "de.sindzinski.lpictrainer.TO";

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    protected static final int REQUEST_CODE_PREFERENCES = 2;

    private static final String TAG = "LPITrainer";
    private SimpleCursorAdapter adapter;

    public String fileName;
    public Integer from;
    public Integer to;
    public Integer max;

    /**
     *     install leakcanary
     */
    private RefWatcher refWatcher = null;

    public RefWatcher getRefWatcher() {
        //Application application = (Application) context.getApplicationContext();
        return refWatcher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //unhandled crash logger
        Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);

        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Logger.logging = true;
        } else {
            Logger.logging = false;
        }
        
        //Load preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        fileName = sharedPref.getString("fileName","").toString();
        from= sharedPref.getInt("from",0);
        to = sharedPref.getInt("to",0);
        max = sharedPref.getInt("max", 0);

        boolean isDarkTheme = sharedPref.getBoolean("pref_key_theme", this.getResources().getBoolean(R.bool.pref_key_dark_default));
        if (isDarkTheme) {
            this.setTheme(android.R.style.Theme_Holo);
        } else {
            this.setTheme(android.R.style.Theme_Holo_Light);
        }
        boolean showAd = sharedPref.getBoolean("pref_key_ads", this.getResources().getBoolean(R.bool.pref_key_ads_default));

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            //todo
            //also in onRetoreInstanceState()
            Fragment restoredFragment = getFragmentManager().getFragment(savedInstanceState,"SavedFragment");
            // new transition probably not needed
            /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, restoredFragment);
            transaction.addToBackStack("restoredFragment");
            transaction.addToBackStack("main");
            transaction.commit();*/
        } else {

            MainFragment mainFragment = MainFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            //transaction.addToBackStack("main");
            transaction.replace(R.id.container, mainFragment);
            transaction.addToBackStack("main");

            // Commit the transaction
            transaction.commit();
            //testFragment.update();

            if (showAd) {
                //ad
                //AdFragment adFragment = new AdFragment();
                Fragment adFragment = new Fragment();
                //Fragment adFragment = Fragment.newInstance();
                FragmentTransaction adTransaction = getFragmentManager().beginTransaction();
                adTransaction.replace(R.id.ad_container, adFragment);
                adTransaction.commit();
            }

            // Check whether the activity is using the layout version with
            // the fragment_container FrameLayout. If so, we must add the first fragment
/*        if (findViewById(R.id.container_two_pane) != null) {
            mTwoPane = true;
            showTestFragment(from, to, fileName);
        } else {
            mTwoPane = false;
        }*/

            //show app rater dialog
            //AppRater.appLaunched(this);
            //Trial.checkTrial(this, false);
        }
        refWatcher = LeakCanary.install(getApplication());
    }

    // implements interface OnTestListener from main fragment
    public void showTestFragment(int from, int to, String fileName, int max) {
        // Create new fragment and transact();

        saveSettings(from, to, fileName, max);

        TestFragment testFragment = (TestFragment)
                getFragmentManager().findFragmentByTag("test");

        if (testFragment != null) {
            //just update the fragment
            //testFragment.update();)) {
            //testFragment = TestFragment.getInstance(from, to, fileName);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.commit();
        } else {
            testFragment = TestFragment.newInstance(from, to, fileName);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

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

    public void saveSettings(int from, int to, String fileName, int max) {

        //editText_file = (EditText) view.findViewById(R.id.editText_file);

        // during onDestroy the fragment is detached from the activity so getActivity returns null!!!!!
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences settings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fileName", fileName );
        editor.putInt("max", max);
        editor.putInt("from", from);
        editor.putInt("to", to);
        // Commit the edits!
        editor.apply();
    }


    public void onPause() {
        super.onPause();
        saveSettings(from, to, fileName, max);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TrainerContract.QuestionEntry.COLUMN_ID, TrainerContract.QuestionEntry.COLUMN_TEXT };
        CursorLoader cursorLoader = new CursorLoader(this,
                TrainerContract.QuestionEntry.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    // We're being destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();

/*        // very important:
        logger.d(TAG, "Destroying helper.");
        if (mHelper != null) mHelper.dispose();
        mHelper = null;*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            getFragmentManager().putFragment(outState, "SavedFragment", fragment);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
    }

    /**
     * Log uncaught Exceptions
     */
    private Thread.UncaughtExceptionHandler handleAppCrash =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    Logger.e("error", ex.toString());
                    //send email here
                }
            };


}
