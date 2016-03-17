package de.sindzinski.lpictrainer;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.sindzinski.lpictrainer.data.Question;
import de.sindzinski.lpictrainer.data.TrainerContract;
import de.sindzinski.util.Logger;

/**
 * Created by steffen on 22.02.16.
 */
public class TestActivity extends FragmentActivity {

    static final int NUM_ITEMS = 10;

    private final static String EXTRA_FILENAME = "de.sindzinski.lpictrainer.FILENAME";
    private final static String EXTRA_FROM = "de.sindzinski.lpictrainer.FROM";
    private final static String EXTRA_TO = "de.sindzinski.lpictrainer.TO";
    private final static String EXTRA_CURRENT = "de.sindzinski.lpictrainer.CURRENT";

    private final String TAG = "TestActivity";

    private String fileName;
    private Integer from;
    private Integer to;
    public int mCurrent;
    public int mNumItems;
    TestAdapter mTestAdapter;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;

    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        Bundle bundle = getIntent().getExtras();

        fileName = bundle.getString(EXTRA_FILENAME);
        from = bundle.getInt(EXTRA_FROM, 0);
        to = bundle.getInt(EXTRA_TO, 0);

                /*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        //getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        Uri mDataUrl = Uri.parse(TrainerContract.QuestionEntry.CONTENT_URI + "/"
                + from + "/" + to);

        String[] mProjection = {
                TrainerContract.QuestionEntry._ID,
                TrainerContract.QuestionEntry.COLUMN_ID,
                TrainerContract.QuestionEntry.COLUMN_TITLE,
        };
        // Defines a string to contain the selection clause
        String mSelectionClause = null;
        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = null;
        // more generic to use selection and args in content provider than here
        String mSortOrder = null;

        Cursor cursor = this.getContentResolver().query(
                mDataUrl,   // The content URI of the question table
                mProjection,                        // The columns to return for each row
                mSelectionClause,                    // Selection criteria
                mSelectionArgs,                     // Selection criteria
                mSortOrder);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mCurrent = cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ID);
                mNumItems = cursor.getCount();
            }
        }

        mTestAdapter = new TestAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mTestAdapter);


//        // Watch for button clicks.
//        ImageButton button = (ImageButton) findViewById(R.id.button_back);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//            }
//        });
//        button = (ImageButton) findViewById(R.id.button_forward);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
//            }
//        });
    }


    public class TestAdapter extends FragmentStatePagerAdapter {
        public TestAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PagerTestFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mNumItems;
        }
    }

    /**
     *
     */
    public static class PagerTestFragment extends Fragment {
        int mCurrent;

        String[] mProjection = {
                TrainerContract.QuestionEntry._ID,
                TrainerContract.QuestionEntry.COLUMN_ID,
                TrainerContract.QuestionEntry.COLUMN_TITLE,
                TrainerContract.QuestionEntry.COLUMN_TYPE,
                TrainerContract.QuestionEntry.COLUMN_POINTS,
                TrainerContract.QuestionEntry.COLUMN_TEXT,
                TrainerContract.QuestionEntry.COLUMN_ANTWORT1,
                TrainerContract.QuestionEntry.COLUMN_RICHTIG1,
                TrainerContract.QuestionEntry.COLUMN_ANTWORT2,
                TrainerContract.QuestionEntry.COLUMN_RICHTIG2,
                TrainerContract.QuestionEntry.COLUMN_ANTWORT3,
                TrainerContract.QuestionEntry.COLUMN_RICHTIG3,
                TrainerContract.QuestionEntry.COLUMN_ANTWORT4,
                TrainerContract.QuestionEntry.COLUMN_RICHTIG4,
                TrainerContract.QuestionEntry.COLUMN_ANTWORT5,
                TrainerContract.QuestionEntry.COLUMN_RICHTIG5,
        };

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static PagerTestFragment newInstance(int current) {
            PagerTestFragment fragment = new PagerTestFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt(EXTRA_CURRENT, current);
            fragment.setArguments(args);

            return fragment;
        }

        public PagerTestFragment() {

        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mCurrent = getArguments() != null ? getArguments().getInt(EXTRA_CURRENT) : 1;

        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_pager_question, container, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

            Uri mDataUrl = Uri.parse(TrainerContract.QuestionEntry.CONTENT_URI + "/"
                    + mCurrent);

            // Defines a string to contain the selection clause
            String mSelectionClause = null;
            // Initializes an array to contain selection arguments
            String[] mSelectionArgs = null;
            // more generic to use selection and args in content provider than here
            String mSortOrder = null;

            Cursor cursor = getActivity().getContentResolver().query(
                    mDataUrl,   // The content URI of the question table
                    mProjection,                        // The columns to return for each row
                    mSelectionClause,                    // Selection criteria
                    mSelectionArgs,                     // Selection criteria
                    mSortOrder);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    mCurrent = cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ID);

                    //ViewHolder viewHolder = (ViewHolder) getView().getTag();

                    viewHolder.textView_current.setText(Integer.toString(mCurrent) + "/" + Integer.toString(mCurrent));
                    //       textView_current.setText(it.nextIndex()-1+"/"+entries.size());

                    viewHolder.textView_question.setText(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TITLE));
                    //TODO the view holder does not keep the default answers, needs to implement
                    //                    .setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TEXT)))
                    //                    .setAntwort1(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)))
                    //                    .setRichtig1(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG1)) > 0)
                    //                    .setAntwort2(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT2)))
                    //                    .setRichtig2(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG2)) > 0)
                    //                    .setAntwort3(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT3)))
                    //                    .setRichtig3(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG3)) > 0)
                    //                    .setAntwort4(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT4)))
                    //                    .setRichtig4(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG4)) > 0)
                    //                    .setAntwort5(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT5)))
                    //                    .setRichtig5(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT5)) > 0)

                    viewHolder.checkBox1.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)));
                    viewHolder.checkBox2.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)));
                    viewHolder.checkBox3.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)));
                    viewHolder.checkBox4.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)));
                    viewHolder.checkBox5.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)));
                    viewHolder.editText1.setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TEXT)));
                }
            }

            return view;
        }

    }

    public static class ViewHolder {

        public final TextView textView_question;
        public final TextView textView_current;


        public final CheckBox checkBox1;
        public final CheckBox checkBox2;
        public final CheckBox checkBox3;
        public final CheckBox checkBox4;
        public final CheckBox checkBox5;
        public final EditText editText1;

        public ViewHolder(View view) {

            textView_question = (TextView) view.findViewById(R.id.textView_current);
            textView_current = (TextView) view.findViewById(R.id.textView_current);

            checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
            checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
            checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
            checkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
            checkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
            editText1 = (EditText) view.findViewById(R.id.editText1);

        }
    }
}

