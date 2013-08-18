package de.sindzinski.lpitrainer;

/**
 * Created by steffen on 18.08.13.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ListIterator;

import de.sindzinski.lpitrainer.AboutDialogFragment;
import de.sindzinski.lpitrainer.DatabaseHandler;
import de.sindzinski.lpitrainer.Entry;
import de.sindzinski.lpitrainer.LegalNoticeDialogFragment;
import de.sindzinski.lpitrainer.R;
import de.sindzinski.lpitrainer.SettingsActivity;
import de.sindzinski.lpitrainer.SettingsFragment;
import de.sindzinski.lpitrainer.TestActivity;
import de.sindzinski.lpitrainer.TestFragment;
import de.sindzinski.lpitrainer.XmlParser;
import android.app.Fragment;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;

public class MainFragment extends Fragment {

    public final static String EXTRA_FILENAME = "de.sindzinski.lpitrainer.FILENAME";
    public final static String EXTRA_FROM = "de.sindzinski.lpitrainer.FROM";
    public final static String EXTRA_TO = "de.sindzinski.lpitrainer.TO";

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    protected static final int REQUEST_CODE_PREFERENCES = 2;

    protected EditText editText_fileName;
    protected SeekBar seekBar_from;
    protected SeekBar seekBar_to;
    protected TextView textView_to;
    protected TextView textView_from;
    protected ImageButton button_file;
    protected Button button_test;

    private static final String TAG = "LPITrainer";

    public ArrayList<Entry> entries = null;

    OnTestListener mListener;

    // Container Activity must implement this interface to receive events from fragment
    public interface OnTestListener {
        public void onTest(int from, int to, String fileName);
    }

    //this ensures that the activity implements the interface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTestListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    //this calls the event on the activity
    public void onTest() {
        // Append the clicked item's row ID with the content provider Uri
        // Send the event and Uri to the host activity
        mListener.onTest(seekBar_from.getProgress(), seekBar_to.getProgress(),  editText_fileName.getText().toString());
    }

    public static MainFragment newInstance() {
        MainFragment f = new MainFragment();

        // Supply index input as an argument.
/*        Bundle args = new Bundle();
        args.putInt("from", from);
        args.putInt("to", to);
        args.putString("fileName", fileName);
        f.setArguments(args);*/

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);

        editText_fileName = (EditText) view.findViewById(R.id.editText_fileName);
        editText_fileName.setText(settings.getString("fileName", "").toString());

        seekBar_from = (SeekBar) view.findViewById(R.id.seekBar_from);
        seekBar_to = (SeekBar) view.findViewById(R.id.seekBar_to);
        seekBar_from.setMax(settings.getInt("max", 0));
        seekBar_from.setProgress(settings.getInt("from", 0));
        seekBar_to.setMax(settings.getInt("max", 0));
        seekBar_to.setProgress(settings.getInt("to", 0));

        textView_to = (TextView) view.findViewById(R.id.textView_to);
        textView_from = (TextView) view.findViewById(R.id.textView_from);
        textView_to.setText(String.valueOf(seekBar_to.getProgress()));
        textView_from.setText(String.valueOf(seekBar_from.getProgress()));

        button_file = (ImageButton) view.findViewById(R.id.button_file);
        button_test = (Button) view.findViewById(R.id.button_test);

        seekBar_to.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                textView_to.setText(String.valueOf(progress));
            }
        });

        seekBar_from.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                textView_from.setText(String.valueOf(progress));
            }
        });

        //register the listener for buttons
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch(v.getId()) {
                    case R.id.button_test:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        onTest();
                        break;
                    case R.id.button_file:
                        selectFile();
                        break;
                }
            }
        };

        button_test.setOnClickListener(clickListener);
        button_file.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        //editText_file = (EditText) view.findViewById(R.id.editText_file);

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getActivity().getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("fileName", editText_fileName.getText().toString());
        editor.putInt("max", seekBar_to.getMax());
        editor.putInt("from", seekBar_from.getProgress());
        editor.putInt("to", seekBar_to.getProgress());
        // Commit the edits!
        editor.commit();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
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
        Intent intentSetPref = new Intent(getActivity(), SettingsActivity.class);
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
        AssetManager am = getActivity().getAssets();
        try {
            InputStream is = am.open("License");
            licenseInfo = convertStreamToString(is);
        } catch (IOException e) {
            Log.e(TAG, "Error reading file: " + e);
        }
        AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(getActivity());
        LicenseDialog.setTitle("Legal Notices");
        LicenseDialog.setMessage(licenseInfo);
        LicenseDialog.show();
    }

    //not used anymore
    public void showAboutDialog() {
        //startActivity(new Intent(this, About.class));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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

    public void selectFile() {
        String fileName = editText_fileName.getText().toString();

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
            Toast.makeText(getActivity(), "@string/message_no_filemanager_installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when the user clicks the Send button
     */
    public void startTest(View view) {

        // Do something in response to button
        //startTestActivity();
        startTestFragment();
    }

    public void startTestFragment() {

        String fileName = editText_fileName.getText().toString();
        // Create new fragment and transaction
        TestFragment testFragment = new TestFragment();
        //testFragment = (TestFragment) getFragmentManager().findFragmentById(R.id.frameLayout);
        //if (testFragment == null || ! testFragment.isInLayout()) {
            testFragment = TestFragment.newInstance(seekBar_from.getProgress(), seekBar_to.getProgress(), editText_fileName.getText().toString());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.frameLayout, testFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        //} else {
            //testFragment.update();
        //}
    }

    /**
     * This is called after the file manager finished.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //editText_file.setText("");

        switch (requestCode) {
            case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            editText_fileName.setText(filePath);
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
                if (resultCode == getActivity().RESULT_OK && data != null) {
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
            DatabaseHandler db = new DatabaseHandler(getActivity());
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

