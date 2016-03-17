package de.sindzinski.lpictrainer;

/**
 * Created by steffen on 18.08.13.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;

import android.app.ProgressDialog;

import de.sindzinski.lpictrainer.data.LoadXmlAsyncTask;
import de.sindzinski.lpictrainer.data.Question;
import de.sindzinski.util.Logger;

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
    private String fileName;
    private Integer from;
    private Integer to;
    private Integer max;
    private static final String TAG = "LPITrainer";

    public ArrayList<Question> entries = null;
    ProgressDialog progressDialog;

    OnTestListener mListener;

    // start test fragment from within this fragment
    // Container Activity must implement this interface to receive events from fragment
    public interface OnTestListener {
        void showTestFragment(int from, int to, String fileName, int max);

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
    public void showTestFragment() {
        // Append the clicked item's row ID with the content provider Uri
        // Send the event and Uri to the host activity
        mListener.showTestFragment(from, to, fileName, max);
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
                        showTestFragment();
                        break;
                    case R.id.button_LPIC1:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        fileName = "lpic1.xml";
                        loadXml(fileName);
                        break;
                    case R.id.button_LPIC2:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        fileName = "lpic2.xml";
                        loadXml(fileName);
                        break;
                    case R.id.button_LPIC3:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        //startTestFragment();
                        fileName = "lpic3.xml";
                        loadXml(fileName);
                        break;
                    case R.id.button_LPIC4:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        fileName = "lpic4.xml";
                        loadXml(fileName);
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
                    Uri fileUri = data.getData();
                    fileName = fileUri.getPath();
                    loadXml(fileName);
                }
                break;

        }
    }

    public void loadXml(final String fileName) {

        // create new instance of async class and override methods for setting ui
        new LoadXmlAsyncTask(getActivity()) {
            @Override
            protected void onPreExecute() {
                //runs in foreground task
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.message_reading_file));
                progressDialog.show();
            }

/*            @Override
            protected void onProgressUpdate(Integer... progress) {
                progressDialog.setProgress(progress[0]);
            }*/
            @Override
            protected void onPostExecute(ArrayList result) {
                //runs in foreground task
                Logger.i(TAG, "running in post execute forground task ");
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

                } else {
                    Toast.makeText(getActivity(), getString(R.string.message_error_reading_file), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(fileName);
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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*MainActivity activity = (MainActivity) getActivity();
        activity.getRefWatcher().watch(this);*/

    }
}

