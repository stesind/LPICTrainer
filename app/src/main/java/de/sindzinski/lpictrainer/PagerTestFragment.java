package de.sindzinski.lpictrainer;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ListIterator;

import de.sindzinski.lpictrainer.data.Answer;
import de.sindzinski.lpictrainer.data.Question;
import de.sindzinski.lpictrainer.data.TrainerContract;
import de.sindzinski.util.Logger;

/**
 * Created by steffen on 19.03.16.
 */
public class PagerTestFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String EXTRA_FILENAME = "de.sindzinski.lpictrainer.FILENAME";
    private final static String EXTRA_FROM = "de.sindzinski.lpictrainer.FROM";
    private final static String EXTRA_TO = "de.sindzinski.lpictrainer.TO";
    private final static String EXTRA_CURRENT = "de.sindzinski.lpictrainer.CURRENT";
    private final static String EXTRA_VIEW_PAGER_INDEX = "de.sindzinski.lpictrainer.VIEW_PAGER_INDEX";
    private static final String CURRENT = "CURRENT_QUESTION";
    private static final String ANSWER = "ANSWER";

    private static final String QUESTION = "QUESTION";
    public static final int loaderQuestion = 0;
    public static final int loaderAnswer = 1;
    int mCurrent;
    int mFrom;
    int mTo;
    int mViewPagerIndex;
    boolean mChecked = false;
    final String TAG = "TestFragment";
    Answer mAnswer;
    Question mQuestion;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static PagerTestFragment newInstance(int current, int from, int to, int viewPagerIndex) {
        PagerTestFragment fragment = new PagerTestFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(EXTRA_CURRENT, current);
        args.putInt(EXTRA_FROM, from);
        args.putInt(EXTRA_TO, to);
        args.putInt(EXTRA_VIEW_PAGER_INDEX, viewPagerIndex);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            //redrawn
            mCurrent = savedInstanceState.getInt(EXTRA_CURRENT, 1);
            mFrom = savedInstanceState.getInt(EXTRA_FROM, 1);
            mTo = savedInstanceState.getInt(EXTRA_TO, 1);
            mViewPagerIndex = savedInstanceState.getInt(EXTRA_VIEW_PAGER_INDEX, 1);
            //mAnswer = savedInstanceState.getParcelable(ANSWER);
            //entries = (ArrayList) savedInstanceState.getSerializable(ENTRIES);
            //mQuestion = savedInstanceState.getParcelable(QUESTION);

        } else {
            mCurrent = getArguments() != null ? getArguments().getInt(EXTRA_CURRENT) : 1;
            mFrom = getArguments() != null ? getArguments().getInt(EXTRA_FROM) : 1;
            mTo = getArguments() != null ? getArguments().getInt(EXTRA_TO) : 1;
            mViewPagerIndex = getArguments() != null ? getArguments().getInt(EXTRA_VIEW_PAGER_INDEX) : 1;

        }
        getLoaderManager().initLoader(loaderQuestion, null, this);
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

//        Button button = (Button) view.findViewById(R.id.button_Test);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                checkAnswer();
//            }
//        });
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;

        if (height <= 1000) {
            viewHolder.textView_question.setTextAppearance(android.R.style.TextAppearance_Small);
            viewHolder.editText1.setTextAppearance(android.R.style.TextAppearance_Small);
            viewHolder.checkBox1.setTextAppearance(android.R.style.TextAppearance_Small);
            viewHolder.checkBox2.setTextAppearance(android.R.style.TextAppearance_Small);
            viewHolder.checkBox3.setTextAppearance(android.R.style.TextAppearance_Small);
            viewHolder.checkBox4.setTextAppearance(android.R.style.TextAppearance_Small);
            viewHolder.checkBox5.setTextAppearance(android.R.style.TextAppearance_Small);
            //viewHolder.textView_current.setTextAppearance(android.R.style.TextAppearance_Small);
        } else {
            viewHolder.textView_question.setTextAppearance(android.R.style.TextAppearance_Medium);
            viewHolder.editText1.setTextAppearance(android.R.style.TextAppearance_Medium);
            viewHolder.checkBox1.setTextAppearance(android.R.style.TextAppearance_Medium);
            viewHolder.checkBox2.setTextAppearance(android.R.style.TextAppearance_Medium);
            viewHolder.checkBox3.setTextAppearance(android.R.style.TextAppearance_Medium);
            viewHolder.checkBox4.setTextAppearance(android.R.style.TextAppearance_Medium);
            viewHolder.checkBox5.setTextAppearance(android.R.style.TextAppearance_Medium);
            //viewHolder.textView_current.setTextAppearance(android.R.style.TextAppearance_Medium);
        }

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

        if (id == loaderQuestion) {
            Uri mDataUrl = Uri.parse(TrainerContract.QuestionEntry.CONTENT_URI + "/"
                    + mCurrent);

            String[] mProjection = {
                    TrainerContract.QuestionEntry._ID,
                    TrainerContract.QuestionEntry.COLUMN_ID,
                    TrainerContract.QuestionEntry.COLUMN_TITLE,
                    TrainerContract.QuestionEntry.COLUMN_TYPE,
                    TrainerContract.QuestionEntry.COLUMN_POINTS,
                    TrainerContract.QuestionEntry.COLUMN_TEXT,
                    TrainerContract.QuestionEntry.COLUMN_ANSWER,
                    TrainerContract.QuestionEntry.COLUMN_ANSWER1,
                    TrainerContract.QuestionEntry.COLUMN_CORRECT1,
                    TrainerContract.QuestionEntry.COLUMN_ANSWER2,
                    TrainerContract.QuestionEntry.COLUMN_CORRECT2,
                    TrainerContract.QuestionEntry.COLUMN_ANSWER3,
                    TrainerContract.QuestionEntry.COLUMN_CORRECT3,
                    TrainerContract.QuestionEntry.COLUMN_ANSWER4,
                    TrainerContract.QuestionEntry.COLUMN_CORRECT4,
                    TrainerContract.QuestionEntry.COLUMN_ANSWER5,
                    TrainerContract.QuestionEntry.COLUMN_CORRECT5,
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
        switch (loader.getId()) {
            case loaderQuestion:
                // do some stuff here

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        try {
                            mQuestion = new Question.Builder()
                                    .setIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ID))))
                                    .setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TITLE)))
                                    .setType(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TYPE)))
                                    .setPoints(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS))))
                                    .setText(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_TEXT)))
                                    .setAnswer(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER)))
                                    .setAnswer1(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER1)))
                                    .setCorrect1(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT1)) > 0)
                                    .setAnswer2(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER2)))
                                    .setCorrect2(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT2)) > 0)
                                    .setAnswer3(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER3)))
                                    .setCorrect3(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT3)) > 0)
                                    .setAnswer4(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER4)))
                                    .setCorrect4(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT4)) > 0)
                                    .setAnswer5(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ANSWER5)))
                                    .setCorrect5(cursor.getInt(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_CORRECT5)) > 0)
                                    .build();
                        } catch (NullPointerException e) {
                            Logger.e(TAG, e.toString());
                        }
                        ViewHolder viewHolder = (ViewHolder) getView().getTag();

//                    TextView textView_current = (TextView) getView().findViewById(R.id.textView_current);
//                    TextView textView_question = (TextView) getView().findViewById(R.id.textView_question);
//                    CheckBox checkBox1 = (CheckBox) getView().findViewById(R.id.checkBox1);
//                    CheckBox checkBox2 = (CheckBox) getView().findViewById(R.id.checkBox2);
//                    CheckBox checkBox3 = (CheckBox) getView().findViewById(R.id.checkBox3);
//                    CheckBox checkBox4 = (CheckBox) getView().findViewById(R.id.checkBox4);
//                    CheckBox checkBox5 = (CheckBox) getView().findViewById(R.id.checkBox5);
//                    EditText editText1 = (EditText) getView().findViewById(R.id.editText1);

                        //viewHolder.textView_current.setText(Integer.toString(mViewPagerIndex) + "/" + Integer.toString(mTo-mFrom));

                        viewHolder.textView_question.setText(mQuestion.text);

                        if ((mQuestion.type != null) && (mQuestion.type.equals("auswahl"))) {

                            viewHolder.checkBox1.setText(mQuestion.answer1);
                            viewHolder.checkBox2.setText(mQuestion.answer2);
                            viewHolder.checkBox3.setText(mQuestion.answer3);
                            viewHolder.checkBox4.setText(mQuestion.answer4);
                            viewHolder.checkBox5.setText(mQuestion.answer5);
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

                mChecked = false;
                break;
            case loaderAnswer:
                // do some other stuff here
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void saveAnswer() {
        ViewHolder viewHolder = (ViewHolder) getView().getTag();
//        CheckBox checkBox1 = (CheckBox) getView().findViewById(R.id.checkBox1);
//        CheckBox checkBox2 = (CheckBox) getView().findViewById(R.id.checkBox2);
//        CheckBox checkBox3 = (CheckBox) getView().findViewById(R.id.checkBox3);
//        CheckBox checkBox4 = (CheckBox) getView().findViewById(R.id.checkBox4);
//        CheckBox checkBox5 = (CheckBox) getView().findViewById(R.id.checkBox5);
//        EditText editText1 = (EditText) getView().findViewById(R.id.editText1);

        //String[] parts = viewHolder.textView_current.getText().toString().split("/");
//        if (parts != null) {
//            int current = Integer.parseInt(parts[0]);
//        }
        mAnswer = new Answer.Builder()
                .setIndex(mCurrent)
                .setChecked(true)
                .setAnswer(viewHolder.editText1.getText().toString())
                .setAnswer1(viewHolder.checkBox1.isChecked())
                .setAnswer2(viewHolder.checkBox2.isChecked())
                .setAnswer3(viewHolder.checkBox3.isChecked())
                .setAnswer4(viewHolder.checkBox4.isChecked())
                .setAnswer5(viewHolder.checkBox5.isChecked())
                .build();

        // check the answers and set points
        Bundle bundle = checkAnswer();
        mAnswer.points = bundle != null ? bundle.getInt("points") : 0;
        mAnswer.checked = mChecked;

        //add new questions
        ContentValues values = new ContentValues();
        values.clear();
        values.put(TrainerContract.AnswerEntry._ID, mAnswer.index);
        values.put(TrainerContract.AnswerEntry.COLUMN_ID, mAnswer.index);
        values.put(TrainerContract.AnswerEntry.COLUMN_CHECKED, mAnswer.checked);
        values.put(TrainerContract.AnswerEntry.COLUMN_POINTS, mAnswer.points);
        values.put(TrainerContract.AnswerEntry.COLUMN_ANSWER, mAnswer.answer);
        values.put(TrainerContract.AnswerEntry.COLUMN_ANSWER1, mAnswer.answer1);
        values.put(TrainerContract.AnswerEntry.COLUMN_ANSWER2, mAnswer.answer2);
        values.put(TrainerContract.AnswerEntry.COLUMN_ANSWER3, mAnswer.answer3);
        values.put(TrainerContract.AnswerEntry.COLUMN_ANSWER4, mAnswer.answer4);
        values.put(TrainerContract.AnswerEntry.COLUMN_ANSWER5, mAnswer.answer5);


//          save to content provider in ui thread
//        try {
//            //insert or replace by "on conflict replace in answer table
//            Uri uri = getActivity().getContentResolver().insert(TrainerContract.AnswerEntry.CONTENT_URI, values);
//            Logger.i(TAG, "Answer Rows inserted: " + uri);
//
//        } catch (Exception e) {
//            Logger.i(TAG, "Exception: " + e.toString());
//        }


        //save to content provider (db) async
        int token = 1;
        AsyncQueryHandler handler =
                new AsyncQueryHandler(getActivity().getContentResolver()) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        super.onInsertComplete(token, cookie, uri);
                        Logger.i(TAG, "Row inserted: " + uri);
                    }

                    @Override
                    protected void onUpdateComplete(int token, Object cookie, int result) {
                        super.onUpdateComplete(token, cookie, result);
                        Logger.i(TAG, "Rows updated: " + result);
                    }
                };
        handler.startInsert(token, null,
                TrainerContract.AnswerEntry.CONTENT_URI,
                values);

//        handler.startUpdate(token, null,
//                TrainerContract.AnswerEntry.CONTENT_URI,
//                values,
//                SelectionClause,
//                SelectionArgs);


    }

    public void markAnswer() {

        ViewHolder viewHolder = (ViewHolder) getView().getTag();

//        CheckBox checkBox1 = (CheckBox) getView().findViewById(R.id.checkBox1);
//        CheckBox checkBox2 = (CheckBox) getView().findViewById(R.id.checkBox2);
//        CheckBox checkBox3 = (CheckBox) getView().findViewById(R.id.checkBox3);
//        CheckBox checkBox4 = (CheckBox) getView().findViewById(R.id.checkBox4);
//        CheckBox checkBox5 = (CheckBox) getView().findViewById(R.id.checkBox5);
//        EditText editText1 = (EditText) getView().findViewById(R.id.editText1);

        if ((mQuestion.type != null) && (mQuestion.type.equals("auswahl"))) {
            if ((mQuestion.correct1 != null) && mQuestion.correct1) {
                if (viewHolder.checkBox1.isChecked() == true) {
                    viewHolder.checkBox1.setTextColor(Color.GREEN);
                } else {
                    viewHolder.checkBox1.setTextColor(Color.RED);
                }
            }
            if ((mQuestion.correct2 != null) && mQuestion.correct2) {
                if (viewHolder.checkBox2.isChecked() == true) {
                    viewHolder.checkBox2.setTextColor(Color.GREEN);
                } else {
                    viewHolder.checkBox2.setTextColor(Color.RED);
                }
            }
            if ((mQuestion.correct3 != null) && mQuestion.correct3) {
                if (viewHolder.checkBox3.isChecked() == true) {
                    viewHolder.checkBox3.setTextColor(Color.GREEN);
                } else {
                    viewHolder.checkBox3.setTextColor(Color.RED);
                }
            }
            if ((mQuestion.correct4 != null) && mQuestion.correct4) {
                if (viewHolder.checkBox4.isChecked() == true) {
                    viewHolder.checkBox4.setTextColor(Color.GREEN);
                } else {
                    viewHolder.checkBox4.setTextColor(Color.RED);
                }
            }
            if ((mQuestion.correct5 != null) && mQuestion.correct5) {
                if (viewHolder.checkBox5.isChecked() == true) {
                    viewHolder.checkBox5.setTextColor(Color.GREEN);
                } else {
                    viewHolder.checkBox5.setTextColor(Color.RED);
                }
            }
        } else {
            if (viewHolder.editText1.getText().toString().trim().equals(mQuestion.answer.trim())) {
                viewHolder.editText1.setTextColor(Color.GREEN);
            } else {
                viewHolder.editText1.setTextColor(Color.RED);
                viewHolder.editText1.setText(mQuestion.answer);
            }
        }
        mChecked = true;
    }

    public Bundle checkAnswer() {
        Integer faults = 0;
        Integer points = 0;
        Bundle bundle = new Bundle();

        if ((mAnswer != null) && (mQuestion != null)) {

            if ((mQuestion.type != null) && (mQuestion.type.equals("auswahl"))) {
                if ((mQuestion.correct1 != null) && (mQuestion.correct1 != mAnswer.answer1)) {
                    faults++;
                }
                if ((mQuestion.correct2 != null) && (mQuestion.correct2 != mAnswer.answer2)) {
                    faults++;
                }
                if ((mQuestion.correct3 != null) && (mQuestion.correct3 != mAnswer.answer3)) {
                    faults++;
                }
                if ((mQuestion.correct4 != null) && (mQuestion.correct4 != mAnswer.answer4)) {
                    faults++;
                }
                if ((mQuestion.correct5 != null) && (mQuestion.correct5 != mAnswer.answer5)) {
                    faults++;
                }
            } else {
                if (!mQuestion.answer.equals(mAnswer.answer)) {
                    faults++;
                }
            }
            if (faults == 0) {
                bundle.putInt("points", mQuestion.points);
                bundle.putInt("maxPoints", mQuestion.points);
                return bundle;
            } else {
                bundle.putInt("points", 0);
                bundle.putInt("maxPoints", mQuestion.points);
                return bundle;
            }
        }
        return null;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the current article selection in case we need to recreate the fragment
        savedInstanceState.putInt(EXTRA_CURRENT, mCurrent);
        savedInstanceState.putInt(EXTRA_FROM, mFrom);
        savedInstanceState.putInt(EXTRA_TO, mTo);
        savedInstanceState.putInt(EXTRA_VIEW_PAGER_INDEX, mViewPagerIndex);
        //savedInstanceState.putSerializable(ANSWERS, answers);
        //savedInstanceState.putSerializable(ENTRIES, entries);
        //savedInstanceState.putParcelable(ANSWER, mAnswer);
        //savedInstanceState.putParcelableArrayList(ENTRIES, entries);
        //savedInstanceState.putParcelable(QUESTION, mQuestion);

    }

    class ViewHolder {

        public final TextView textView_question;
        //public final TextView textView_current;


        public final CheckBox checkBox1;
        public final CheckBox checkBox2;
        public final CheckBox checkBox3;
        public final CheckBox checkBox4;
        public final CheckBox checkBox5;
        public final EditText editText1;

        public ViewHolder(View view) {

            textView_question = (TextView) view.findViewById(R.id.textView_question);
            //textView_current = (TextView) view.findViewById(R.id.textView_current);

            checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
            checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
            checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
            checkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
            checkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
            editText1 = (EditText) view.findViewById(R.id.editText1);

        }
    }
}
