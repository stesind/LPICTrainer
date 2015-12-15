package de.sindzinski.lpictrainer;

/**
 * Created by steffen on 18.08.13.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import android.os.AsyncTask;
import android.app.ProgressDialog;

import de.sindzinski.database.DatabaseHandler;
import de.sindzinski.database.XmlParser;
import de.sindzinski.helper.Logger;

public class MainFragment extends Fragment {

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;

    protected EditText editText_fileName;
    protected SeekBar seekBar_from;
    protected SeekBar seekBar_to;
    protected TextView textView_to;
    protected TextView textView_from;
    protected ImageButton button_file;
    protected Button button_test;
    protected Button button_LPIC1;
    protected Button button_LPIC2;
    protected Button button_LPIC3;
    protected Button button_LPIC4;
    public String fileName;
    public Integer from;
    public Integer to;
    public Integer max;
    private static final String TAG = "LPITrainer";

    public ArrayList<Entry> entries = null;
    ProgressDialog progressDialog;

    OnTestListener mListener;

        // Container Activity must implement this interface to receive events from fragment
    public interface OnTestListener {
        void onTest(int from, int to, String fileName, int max);

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
        mListener.onTest(from, to, fileName, max);
    }

    public static MainFragment newInstance() {
        MainFragment f = new MainFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);


        //not already done in main activity
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, container, false);



        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        fileName = sharedPref.getString("fileName","").toString();
        from= sharedPref.getInt("from",0);
        to = sharedPref.getInt("to",0);
        max = sharedPref.getInt("max",0);

        Uri fileUri = Uri.parse(sharedPref.getString("fileName", ""));

        editText_fileName = (EditText) view.findViewById(R.id.editText_fileName);
        //editText_fileName.setText(settings.getString("fileName", "").toString());
        editText_fileName.setText(fileUri.getLastPathSegment());

        seekBar_from = (SeekBar) view.findViewById(R.id.seekBar_from);
        seekBar_to = (SeekBar) view.findViewById(R.id.seekBar_to);
        seekBar_from.setMax(max);
        seekBar_from.setProgress(from);
        seekBar_to.setMax(max);
        seekBar_to.setProgress(to);

        textView_to = (TextView) view.findViewById(R.id.textView_to);
        textView_from = (TextView) view.findViewById(R.id.textView_from);
        textView_to.setText(String.valueOf(seekBar_to.getProgress()));
        textView_from.setText(String.valueOf(seekBar_from.getProgress()));



        button_file = (ImageButton) view.findViewById(R.id.button_file);
        button_test = (Button) view.findViewById(R.id.button_test);
        button_LPIC1 = (Button) view.findViewById(R.id.button_LPIC1);
        button_LPIC2 = (Button) view.findViewById(R.id.button_LPIC2);
        button_LPIC3 = (Button) view.findViewById(R.id.button_LPIC3);
        button_LPIC4 = (Button) view.findViewById(R.id.button_LPIC4);


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
                to = Integer.parseInt(String.valueOf(progress));
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
                from = Integer.parseInt(String.valueOf(progress));
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
                    case R.id.button_LPIC1:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        new loadXmlFromAssetTask().execute("lpic1.xml");
                        break;
                    case R.id.button_LPIC2:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        new loadXmlFromAssetTask().execute("lpic2.xml");
                        break;
                    case R.id.button_LPIC3:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        new loadXmlFromAssetTask().execute("lpic3.xml");
                        break;
                    case R.id.button_LPIC4:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.

                        new loadXmlFromAssetTask().execute("lpic4.xml");
                        break;
                    case R.id.button_file:
                        //selectFile();
                        showFileChooser();
                        break;
                }
            }
        };

        button_test.setOnClickListener(clickListener);
        button_LPIC1.setOnClickListener(clickListener);
        button_LPIC2.setOnClickListener(clickListener);
        button_LPIC3.setOnClickListener(clickListener);
        button_LPIC4.setOnClickListener(clickListener);
        button_file.setOnClickListener(clickListener);

        return view;
    }

    public void selectFile() {
        //String fileName = editText_fileName.getText().toString();

        Intent intent = new Intent("org.openintents.action.PICK_FILE");

        if (!TextUtils.isEmpty(fileName)) {
            // Construct URI from file name.
            File file = new File(fileName);
            intent.setData(Uri.fromFile(file));
            //intent.setData(Uri.parse(fileName));
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
            Logger.e(TAG, "Error reading file: " + e);
            Toast.makeText(getActivity(), "@string/message_no_filemanager_installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("FILE/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select txt file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this.getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is called after the file manager finished.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //editText_file.setText("");

        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Uri fileUri = data.getData();
                    fileName = fileUri.getPath();
                    //try {
                            new loadXmlFromFileTask().execute(fileName);
                    //} catch (FileNotFoundException e) {
                        /* Exception handler must be here! */
                    //}
                }
                break;

            case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        fileName = fileUri.getPath();
                        if (fileName != null) {

                                //load in async task
                                new loadXmlFromFileTask().execute(fileName);

                        }
                    }
                }
                break;
        }
    }

    private class loadXmlFromFileTask extends AsyncTask<String, Integer, ArrayList<Entry>>  {

        protected ArrayList<Entry> doInBackground(String... fileName) {
            //runs in background task
            Logger.e(TAG, "running in async background task: doInBackground " );
            try {
                entries = loadXmlFromFile(fileName[0]);
                return entries;
            } catch (IOException ie){
                Logger.e(TAG, "Error: " +ie.toString() );
            } catch (XmlPullParserException ie){
                Logger.e(TAG, "Error: " +ie.toString() );
            }
            return null;
        }
        protected void onPreExecute() {
            //runs in foreground task
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.message_reading_file));
            progressDialog.show();
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(ArrayList result) {
            //runs in foreground task
            Logger.e(TAG, "running in post execute forground task " );
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //showDialog("Downloaded " + result + " bytes");
            if (result != null) {
                entries = result;
                Uri fileUri = Uri.parse(fileName);
                editText_fileName.setText(fileUri.getLastPathSegment());
                seekBar_from.setMax(entries.size());
                seekBar_from.setProgress(0);
                from = 0;
                seekBar_to.setMax(entries.size());
                seekBar_to.setProgress(entries.size());
                to = entries.size();
                max = entries.size();
                safeToSQL(entries);
            } else {
                Toast.makeText(getActivity(), getString(R.string.message_error_reading_file), Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<Entry> loadXmlFromFile(String fileName) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            XmlParser parser = new XmlParser();
            //ArrayList<Entry> entries = null;

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

        protected void safeToSQL(ArrayList<Entry> entries) {

            ListIterator it = entries.listIterator();
            try {
                DatabaseHandler db = new DatabaseHandler(getActivity());
                db.onWipe();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    db.addEntry(entry);
                }
            } catch (SQLiteException e) {
                Logger.e(TAG, "Error reading database: " + e);
            }
        }
    }

    private class loadXmlFromAssetTask extends AsyncTask<String, Integer, ArrayList<Entry>>
    {
        String fileName;
        protected ArrayList<Entry> doInBackground(String... params) {
            //runs in background task

            this.fileName = params[0];

            Logger.e(TAG, "running in async background task: doInBackground " );
            try {
                entries = loadXmlFromAsset(params[0]);
                return entries;
            } catch (IOException ie){
                Logger.e(TAG, "Error: " +ie.toString() );
            } catch (XmlPullParserException ie){
                Logger.e(TAG, "Error: " +ie.toString() );
            }
            return null;
        }
        protected void onPreExecute() {
            //runs in foreground task
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.message_reading_file));
            progressDialog.show();
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(ArrayList result) {
            //runs in foreground task
            Logger.e(TAG, "running in post execute forground task ");
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //showDialog("Downloaded " + result + " bytes");
            if (result != null) {
                entries = result;
                editText_fileName.setText(this.fileName);
                seekBar_from.setMax(entries.size());
                seekBar_from.setProgress(0);
                from = 0;
                seekBar_to.setMax(entries.size());
                seekBar_to.setProgress(entries.size());
                to = entries.size();
                max = entries.size();
                safeToSQL(entries);
            } else {
                Toast.makeText(getActivity(), getString(R.string.message_error_reading_file), Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<Entry> loadXmlFromAsset(String fileName) throws XmlPullParserException, IOException {
            InputStream is = null;
            // Instantiate the parser
            XmlParser parser = new XmlParser();
            //ArrayList<Entry> entries = null;

            BufferedInputStream bis = null;
            DataInputStream dis = null;

            try {
                is = getActivity().getAssets().open(fileName);
                bis = new BufferedInputStream(is);
                dis = new DataInputStream(bis);

                entries = parser.parse(dis);
                return entries;

            } finally {
                if (is != null) {
                    is.close();
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

        protected void safeToSQL(ArrayList<Entry> entries) {

            ListIterator it = entries.listIterator();
            try {
                DatabaseHandler db = new DatabaseHandler(getActivity());
                db.onWipe();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    db.addEntry(entry);
                }
            } catch (SQLiteException e) {
                Logger.e(TAG, "Error reading database: " + e);
            }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        //outState.putInt(CURRENT, current);
    }

    //extended log tag
    private static final boolean FINAL_CONSTANT_IS_LOCAL = true;
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private String getLogTagWithMethod() {
        if (FINAL_CONSTANT_IS_LOCAL) {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement[] trace = stack.getStackTrace();
            return trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
        } else {
            return LOG_TAG;
        }
    }
}

