package de.sindzinski.lpitrainer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
//import com.google.android.gms.common.GooglePlayServicesUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.ListIterator;

import android.util.Log;
import android.content.Context;

import java.io.FileReader;

import android.content.res.AssetManager;
import android.preference.PreferenceFragment;
import android.app.DialogFragment;
import android.app.Fragment;
import 	android.app.FragmentTransaction;

//import org.openintents.intents.FileManagerIntents;

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


    public ArrayList<Entry> entries = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startMainFragment();
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
            transaction.replace(R.id.frameLayout, testFragment);
            transaction.addToBackStack("test");

            // Commit the transaction
            transaction.commit();
        //} else {
            //testFragment.update();
        //}
    }

    public void startMainFragment() {

        // Create new fragment and transaction
        MainFragment mainFragment = new MainFragment();
        mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.frameLayout);
        if (mainFragment == null || ! mainFragment.isInLayout()) {
            mainFragment = MainFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.frameLayout, mainFragment);
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
/*
        editText_file = (EditText) findViewById(R.id.editText_file);

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("fileName", editText_file.getText().toString());
        editor.putInt("max", seekBar_to.getMax());
        editor.putInt("from", seekBar_from.getProgress());
        editor.putInt("to", seekBar_to.getProgress());
        // Commit the edits!
        editor.commit();*/

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
                showSettingsActivity();
                //showSettingsFragment();
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
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public void showSettingsActivity() {
        Intent intentSetPref = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(intentSetPref, REQUEST_CODE_PREFERENCES);
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
    /*}

    public void selectFile(View view) {
        String fileName = editText_file.getText().toString();

        Intent intent = new Intent("org.openintents.action.PICK_FILE");

        if (!TextUtils.isEmpty(fileName)) {
            // Construct URI from file name.
            File file = new File(fileName);
            intent.setData(Uri.fromFile(file));
        } else {
            intent.setData(Uri.parse("file://storage/extSdCard/lpic4.xml"));
        }

        //intent.putExtra("org.openintents.extra.TITLE", "Please select a file");

        // Set fancy title and button (optional)
        //intent.putExtra("org.openintents.extra.TITLE", "@string/button_open_file");
        intent.putExtra("org.openintents.extra.TITLE", getResources().getString(R.string.button_open_file));

        //intent.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, getString(R.string.open_button));

        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);

        } catch (ActivityNotFoundException e) {
            // No compatible file manager was found.
            Log.e(TAG, "Error reading file: " + e);
            Toast.makeText(this, "@string/message_no_filemanager_installed",
                    Toast.LENGTH_SHORT).show();
        }
 */   }

    /**
     * Called when the user clicks the Send button
     */
    public void startTest(View view) {

        // Do something in response to button
        //startTestActivity();
        startTestFragment();
    }

    public void startTestFragment() {

        EditText editText = (EditText) findViewById(R.id.editText_fileName);
        String fileName = editText.getText().toString();
        // Create new fragment and transaction
        TestFragment testFragment = new TestFragment();
        testFragment = (TestFragment) getFragmentManager().findFragmentById(R.id.frameLayout);
        if (testFragment == null || ! testFragment.isInLayout()) {
            testFragment = TestFragment.newInstance(seekBar_from.getProgress(), seekBar_to.getProgress(), fileName);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.frameLayout, testFragment);
            //transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else {
            //testFragment.update();
        }
    }

    public void startTestActivity() {

        // Do something in response to button
        Intent intent = new Intent(this, TestActivity.class);

        EditText editText = (EditText) findViewById(R.id.editText_fileName);
        String fileName = editText.getText().toString();

        intent.putExtra(EXTRA_FILENAME, fileName);
        intent.putExtra(EXTRA_FROM, seekBar_from.getProgress());
        intent.putExtra(EXTRA_TO, seekBar_to.getProgress());
        startActivity(intent);
    }

    /**
     * This is called after the file manager finished.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //editText_file.setText("");

        switch (requestCode) {
            case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            editText_file.setText(filePath);
                            try {
                                entries = loadXmlFromFile(filePath);

                                safeToSQL(entries);
                                seekBar_from.setMax(entries.size());
                                seekBar_from.setProgress(0);
                                seekBar_to.setMax(entries.size());
                                seekBar_to.setProgress(entries.size());

                            } catch (IOException e) {
                                Log.e(TAG, "Error reading file: " + e);
                            } catch (XmlPullParserException e) {
                                Log.e(TAG, "Error reading file: " + e);
                            }
                        }
                    }
                }
                break;
            case REQUEST_CODE_PREFERENCES:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the preferences

                }
                break;

        }
    }

    //reads xml from file
    //
    private ArrayList<Entry> loadXmlFromFile(String fileName) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        XmlParser parser = new XmlParser();
        ArrayList<Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;

        //EditText editText = (EditText) findViewById(R.id.editText_file);
        //String fileName = editText.getText().toString();

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(fileName);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            entries = parser.parse(dis);
            return entries;

        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (dis != null) {
                dis.close();
            }
        }

        //Toast.makeText(this, entries.size(),
        //Toast.LENGTH_SHORT).show();
    }

    public void safeToSQL(ArrayList<Entry> entries) {

        ListIterator it = entries.listIterator();
        try {
            DatabaseHandler db = new DatabaseHandler(this);
            db.onWipe();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                db.addEntry(entry);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error reading database: " + e);
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
