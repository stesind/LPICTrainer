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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    TestFragmentStatePagerAdapter mAdapter;

    ViewPager mPager;

    public static final int loaderID = 1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        Bundle bundle = getIntent().getExtras();

        fileName = bundle.getString(EXTRA_FILENAME);
        from = bundle.getInt(EXTRA_FROM, 0);
        to = bundle.getInt(EXTRA_TO, 0);

        mAdapter = new TestFragmentStatePagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);

        // Watch for button clicks.
        ImageButton button = (ImageButton) findViewById(R.id.button_back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        button = (ImageButton) findViewById(R.id.button_forward);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });
    }

    public class TestFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public TestFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return to;
        }

        @Override
        public Fragment getItem(int current) {
            return PagerTestFragment.newInstance(current, to);
        }
    }

    public static class PagerTestFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor> {

        int mCurrent;
        int mTo;
        final String TAG = "TestFragment";

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static PagerTestFragment newInstance(int current, int to) {
            PagerTestFragment fragment = new PagerTestFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt(EXTRA_CURRENT, current);
            args.putInt(EXTRA_TO, to);
            fragment.setArguments(args);

            return fragment;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mCurrent = getArguments() != null ? getArguments().getInt(EXTRA_CURRENT) : 1;
            mTo = getArguments() != null ? getArguments().getInt(EXTRA_TO) : 1;
            getLoaderManager().initLoader(loaderID, null, this);
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
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
//            setListAdapter(new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            if (id == loaderID) {
                Uri mDataUrl = Uri.parse(TrainerContract.QuestionEntry.CONTENT_URI + "/"
                        + mCurrent);

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

                // Defines a string to contain the selection clause
                String mSelectionClause = null;
                // Initializes an array to contain selection arguments
                String[] mSelectionArgs = null;
                // more generic to use selection and args in content provider than here
                String mSortOrder = null;

                return new CursorLoader(
                        getActivity(),
                        mDataUrl,   // The content URI of the question table
                        mProjection,                        // The columns to return for each row
                        mSelectionClause,                    // Selection criteria
                        mSelectionArgs,                     // Selection criteria
                        mSortOrder);
            } else {
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Question question;


            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    question = new Question.Builder()
                            .setIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ID))))
                            .setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TITLE)))
                            .setType(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TYPE)))
                            .setPoints(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS))))
                            .setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TEXT)))
                            .setAntwort1(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT1)))
                            .setRichtig1(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG1)) > 0)
                            .setAntwort2(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT2)))
                            .setRichtig2(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG2)) > 0)
                            .setAntwort3(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT3)))
                            .setRichtig3(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG3)) > 0)
                            .setAntwort4(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT4)))
                            .setRichtig4(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_RICHTIG4)) > 0)
                            .setAntwort5(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT5)))
                            .setRichtig5(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANTWORT5)) > 0)
                            .build();

                    ViewHolder viewHolder = (ViewHolder) getView().getTag();

//                    TextView textView_current = (TextView) getView().findViewById(R.id.textView_current);
//                    TextView textView_question = (TextView) getView().findViewById(R.id.textView_question);
//                    CheckBox checkBox1 = (CheckBox) getView().findViewById(R.id.checkBox1);
//                    CheckBox checkBox2 = (CheckBox) getView().findViewById(R.id.checkBox2);
//                    CheckBox checkBox3 = (CheckBox) getView().findViewById(R.id.checkBox3);
//                    CheckBox checkBox4 = (CheckBox) getView().findViewById(R.id.checkBox4);
//                    CheckBox checkBox5 = (CheckBox) getView().findViewById(R.id.checkBox5);
//                    EditText editText1 = (EditText) getView().findViewById(R.id.editText1);

                    viewHolder.textView_current.setText(Integer.toString(mCurrent) + "/" + Integer.toString(mTo));

                    viewHolder.textView_question.setText(question.text);

                    if ((question.type != null) && (question.type.equals("auswahl"))) {

                        viewHolder.checkBox1.setText(question.antwort1);
                        viewHolder.checkBox2.setText(question.antwort2);
                        viewHolder.checkBox3.setText(question.antwort3);
                        viewHolder.checkBox4.setText(question.antwort4);
                        viewHolder.checkBox5.setText(question.antwort5);
                        viewHolder.editText1.setText("");

                        viewHolder.checkBox1.setVisibility(View.VISIBLE);
                        viewHolder.checkBox2.setVisibility(View.VISIBLE);
                        viewHolder.checkBox3.setVisibility(View.VISIBLE);
                        viewHolder.checkBox4.setVisibility(View.VISIBLE);
                        viewHolder.checkBox5.setVisibility(View.VISIBLE);
                        viewHolder.editText1.setVisibility(View.GONE);
                    } else {
                        viewHolder.checkBox1.setText("");
                        viewHolder.checkBox2.setText("");
                        viewHolder.checkBox3.setText("");
                        viewHolder.checkBox4.setText("");
                        viewHolder.checkBox5.setText("");
                        viewHolder.editText1.setText("");
                        viewHolder.checkBox1.setVisibility(View.GONE);
                        viewHolder.checkBox2.setVisibility(View.GONE);
                        viewHolder.checkBox3.setVisibility(View.GONE);
                        viewHolder.checkBox4.setVisibility(View.GONE);
                        viewHolder.checkBox5.setVisibility(View.GONE);
                        viewHolder.editText1.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    static class ViewHolder {

        public final TextView textView_question;
        public final TextView textView_current;


        public final CheckBox checkBox1;
        public final CheckBox checkBox2;
        public final CheckBox checkBox3;
        public final CheckBox checkBox4;
        public final CheckBox checkBox5;
        public final EditText editText1;

        public ViewHolder(View view) {

            textView_question = (TextView) view.findViewById(R.id.textView_question);
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

