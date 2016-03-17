package de.sindzinski.lpictrainer;

/**
 * Created by steffen on 17.08.13.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.preference.PreferenceManager;

import java.util.*;

import android.view.GestureDetector;

import de.sindzinski.lpictrainer.data.Question;
import de.sindzinski.lpictrainer.data.Answer;
import de.sindzinski.lpictrainer.data.TrainerContract;
import de.sindzinski.util.Logger;

public class TestFragment extends Fragment  {

    public ArrayList<Question> questions = null;
    //public ArrayList <Answer> answers = null;
    public HashMap answers = new HashMap();
    private Integer from;
    private Integer to;
    private String fileName;
    public ListIterator<Question> it;

    protected TextView textView_question;
    protected TextView textView_current;
    protected ScrollView scrollView;
    protected LinearLayout linearLayout;
    protected RelativeLayout relativeLayout;

    protected ImageButton buttonForward;
    protected ImageButton buttonBack;
    protected ImageButton buttonCheck;
    protected LinearLayout linearLayoutContainer;
    protected CheckBox checkBox1;
    protected CheckBox checkBox2;
    protected CheckBox checkBox3;
    protected CheckBox checkBox4;
    protected CheckBox checkBox5;
    protected EditText editText1;

    protected Integer current = 1;
    public String lastAction = null;
    public Integer max = 0;
    protected Question question = null;

    protected Boolean answered = false;
    protected Boolean checked = false;

    private static final String TAG="LPITrainer";
    private static final String CURRENT="CURRENT_QUESTION";
    private static final String ANSWERS="CURRENT_ANSWERS";

    private static final String QUESTIONS="CURRENT_QUESTIONS";
    public static final String ARG_FROM="from";
    public static final String ARG_TO="to";
    public static final String ARG_FILENAME="fileName";

    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private int textSize = 2;
    private int textSizeResource;

    private int mTouchSlop;
    private int mMinimumFlingVelocity;

    // pass data to the fragment using factory pattern
    // later get date in onCreate with getArguments
    public static TestFragment newInstance(Integer from, Integer to, String fileName) {
        TestFragment testFragment = new TestFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("from", from);
        args.putInt("to", to);
        args.putString("fileName", fileName);
        testFragment.setArguments(args);

        return testFragment;
    }

    public TestFragment() {

    }

    //needed for adding items to the menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //setRetainInstance(true); //savedInstancestate will always be null!!!

        //get Arguments from bundle
        from = getArguments().getInt(ARG_FROM, 0);
        to = getArguments().getInt(ARG_TO, 0);
        fileName = getArguments().getString(ARG_FILENAME);

        //only valid settings
        if (from > to) {
            from = 0;
        }

        //get default preferences from preferencemanager not from shared settings
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //delete the old string key size because it is now integer
        //sharedPref.edit().remove("pref_key_size").commit();

        boolean shuffle = sharedPref.getBoolean("pref_key_shuffle", getActivity().getResources().getBoolean(R.bool.pref_key_shuffle_default));
        textSize = Integer.parseInt(sharedPref.getString("pref_key_size", getActivity().getResources().getString(R.string.pref_key_size_default)));

        switch (textSize) {
            case 1:
                textSizeResource = android.R.style.TextAppearance_Small;
                break;
            case 2:
                textSizeResource = android.R.style.TextAppearance_Medium;
                break;
            case 3:
                textSizeResource = android.R.style.TextAppearance_Large;
                break;
        }

        // onCreated is only called if fragment was created or app was in background, or screen rotated, safedinstancestate is then filled by the safeinstancestate method
        //onCreateView instead is called also if other fragment like settings fragment was in foreground
        if (savedInstanceState != null) {
            //redrawn
            current = savedInstanceState.getInt(CURRENT,0);
            answers = (HashMap) savedInstanceState.getSerializable(ANSWERS);
            //entries = (ArrayList) savedInstanceState.getSerializable(ENTRIES);
            questions = (ArrayList) savedInstanceState.getSerializable(QUESTIONS);
        } else {
            //newly drawn
            //load first data to display
//            try {
//                //load from sqlite database
//                //DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
//                //old databasehelper
//                //entries = (ArrayList) db.getAllEntries(fileName);
//                //new content provider
///*                entries = (ArrayList) getData();
//                subEntries = new ArrayList<Question>(entries.subList((from > to) ? 0 : from, to));*/
//                questions = (ArrayList) getData();
//                //subEntries = new ArrayList<Question>(entries.subList((from > to) ? 0 : from, to));
//                //if set in preferences then shuffle entries
//
//                //Logger.i(TAG, "Rows added: " + QuestionUri);
//
//
//            } catch (SQLiteException e) {
//                Log.e(TAG, "Error reading database: " + e);
//            }

            questions = (ArrayList) getData();
            //subEntries = new ArrayList<Question>(entries.subList((from > to) ? 0 : from, to));
            if (shuffle) {
                Collections.shuffle(questions);
            }
        }

        //entries = loadXmlFromFile();
        //check if from is <= to


        //get the iterator fr
        it = questions.listIterator();

        //run to current item
        int i = 1;

        while (i < current) {
            it.next();
            i++;
        }
        max = to-from;
        if (current > 0) {
            current--; //neccessary because current is increased in nextQuestion
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //not already done in main activity
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.test_fragment, container, false);

        textView_current = (TextView) view.findViewById(R.id.textView_current);
        textView_question = (TextView) view.findViewById(R.id.textView_question);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        linearLayoutContainer = (LinearLayout) view.findViewById(R.id.linearLayoutContainer);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);

/*        //animation of layout changes
        //can also be done in xml layout by android:animateLayoutChanges="trtransaction.commit();ue"
        LayoutTransition layoutTransition = new LayoutTransition();
        //required min api level 16!
        //setupAnimations(layoutTransition);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        linearLayoutContainer.setLayoutTransition(layoutTransition);*/

        buttonBack = (ImageButton) view.findViewById(R.id.button_back);
        buttonCheck = (ImageButton) view.findViewById(R.id.button_check);
        buttonForward = (ImageButton) view.findViewById(R.id.button_forward);

        //register the listener for buttons
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch(v.getId()) {
                    case R.id.button_forward:
                        // which is supposed to be called automatically
                        // in your activity, which has now changed to a fragment.
                        nextQuestion();
                        break;
                    case R.id.button_back:
                        prevQuestion();
                        break;
                    case R.id.button_check:
                        checkAnswer();
                        break;
                }
            }
        };

        buttonForward.setOnClickListener(clickListener);
        buttonBack.setOnClickListener(clickListener);
        buttonCheck.setOnClickListener(clickListener);

        ViewConfiguration vc = ViewConfiguration.get(getActivity());
        mTouchSlop = vc.getScaledTouchSlop();
        mMinimumFlingVelocity = vc.getScaledMinimumFlingVelocity();

        //working gesture detecture
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    private MotionEvent mLastOnDownEvent;

                    @Override
                    public boolean onDown(MotionEvent e) {
                        Logger.i(TAG, "onDown has been called!");
                        mLastOnDownEvent = e;
                        //return super.onDown(e);
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Logger.i(TAG, "onFling has been called!");
                        if (e1==null) {
                            Logger.i(TAG, "onFling - e1 = null e2 = " + e2.getX() + "velocityX: " + velocityX);
                        } else {
                            Logger.i(TAG, "onFling - e1 = " + e1.getX() + " e2 = " + e2.getX() + "  distance X " + (e1.getX() - e2.getX()) + "velocityX: " + velocityX);
                        }


                        //final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MIN_DISTANCE = mTouchSlop;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        //final int SWIPE_THRESHOLD_VELOCITY = 200;
                        //final int SWIPE_THRESHOLD_VELOCITY = 0;
                        final int SWIPE_THRESHOLD_VELOCITYnull = mMinimumFlingVelocity;


                        try {

                            if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                if (velocityX > 0) {
                                    Logger.i(TAG, "Left to Right" );
                                    prevQuestion();
                                } else {
                                    Logger.i(TAG, "Right to Left" );
                                    nextQuestion();
                                }
                            }
                        } catch (Exception e) {
                            // nothing
                            //Logger.i(TAG, "Exception" + e.getMessage().toString());
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }

        });


        currentQuestion();

        changeTextSize();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.test, menu);
        inflater.inflate(R.menu.zoom, menu);
        //MenuItem end = menu.add("@string/menu_end");
        //end.setIcon(R.drawable.ic_menu_refresh);
    }

    private List<Question> getData()
    {


        List<Question> entryList = new ArrayList<Question>();

        // A "projection" defines the columns that will be returned for each row

        String[] mProjection = {
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

        Uri uri = Uri.parse(TrainerContract.QuestionEntry.CONTENT_URI + "/"
                + from + "/" + to);
        // Defines a string to contain the selection clause
        String mSelectionClause = null;
        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = null;
        // more generic to use selection and args in content provider than here
        String mSortOrder = null;
/*        String mSelectionClause = "((" + QuestionTable.COLUMN_ID  + " >= ?) AND ("
                + QuestionTable.COLUMN_ID + " = ?))";
        String[] mSelectionArgs = new String[] {uri.getPathSegments().get(uri.getPathSegments().size()-2), uri.getLastPathSegment(),
                "thx4uall@gmail.com"};*/
        try (Cursor cursor = getActivity().getContentResolver().query(
                uri,   // The content URI of the question table
                mProjection,                        // The columns to return for each row
                mSelectionClause,                    // Selection criteria
                mSelectionArgs,                     // Selection criteria
                mSortOrder)) {

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Question question = new Question.Builder()
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
                        // Adding contact to list
                        entryList.add(question);
                    } while (cursor.moveToNext());
                }

                // always close the cursor
                cursor.close();
            }
            return entryList;
        } catch (SQLiteException e) {
            Logger.e(TAG, "Error reading database: " + e);
        }
        return null;
    }

    public void nextTextSize() {
        if (textSize <3) {
            textSize++;
        } else {
            textSize = 1;
        }
        switch (textSize) {
            case 1:
                textSizeResource = android.R.style.TextAppearance_Small;
                break;
            case 2:
                textSizeResource = android.R.style.TextAppearance_Medium;
                break;
            case 3:
                textSizeResource = android.R.style.TextAppearance_Large;
                break;
        }
        changeTextSize();
    }

    public void changeTextSize() {

        ArrayList<View> allViewsWithinMyTopView = getAllChildren(relativeLayout);
        for (View child : allViewsWithinMyTopView) {
            if (child instanceof TextView) {
                TextView childView = (TextView) child;
                switch (textSize) {
                    case 1:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                        break;
                    case 2:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                        break;
                    case 3:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                        break;
                }
            }
            if (child instanceof EditText) {
                EditText childView = (EditText) child;
                switch (textSize) {
                    case 1:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                        break;
                    case 2:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                        break;
                    case 3:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                        break;
                }
            }
            if (child instanceof CheckBox) {
                CheckBox childView = (CheckBox) child;
                switch (textSize) {
                    case 1:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                        break;
                    case 2:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                        break;
                    case 3:
                        childView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                        break;
                }
            }
        }
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch (item.getItemId()) {
            /*case R.id.help:
                //startActivity(new Intent(this, Help.class));
                return true;*/
            case R.id.end:
                //startActivity(new Intent(this, Help.class));
                onEnd();
                return true;
            case R.id.zoom:
                //startActivity(new Intent(this, Help.class));
                //Toast.makeText(getActivity(), "zoom", Toast.LENGTH_LONG).show();
                nextTextSize();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addWidgets() {
        if ((question.type !=null) && (question.type.equals("auswahl"))) {
            checkBox1 = new CheckBox(getActivity());
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
                checkBox1.setText("test");
                checkBox1.setTextAppearance(getActivity(), textSizeResource);
            linearLayoutContainer.addView(checkBox1);
            checkBox2 = new CheckBox(getActivity());
            checkBox2.setTextAppearance(getActivity(), textSizeResource);
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox2.setText("test");
            linearLayoutContainer.addView(checkBox2);
            checkBox3 = new CheckBox(getActivity());
            checkBox3.setTextAppearance(getActivity(), textSizeResource);
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox3.setText("test");
            linearLayoutContainer.addView(checkBox3);
            checkBox4 = new CheckBox(getActivity());
            checkBox4.setTextAppearance(getActivity(), textSizeResource);
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox4.setText("test");
            linearLayoutContainer.addView(checkBox4);
            checkBox5 = new CheckBox(getActivity());
            checkBox5.setTextAppearance(getActivity(), textSizeResource);
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox5.setText("test");
            linearLayoutContainer.addView(checkBox5);

        } else {
            editText1 = new EditText(getActivity());
            linearLayoutContainer.addView(editText1);
            editText1.setTextAppearance(getActivity(), textSizeResource);
            editText1.setText("");
        }
    }

    public void clearView() {
        linearLayoutContainer.removeAllViews();
    }

    public void prevQuestion() {
        saveAnswers();
        if (it.hasPrevious() & (current > 1)) {
            if (lastAction == "next") {
                question = it.previous();
            }
            question = it.previous();
            lastAction = "previous";
            Logger.d(TAG, String.valueOf(question.index));
            current--;
            setQuestion(question);
            loadAnswers();
            if (checked) {
                checkAnswer();
            }
            //changeTextSize();
        }
    }

    public void nextQuestion() {
        saveAnswers();
        if (it.hasNext()) {
            if (lastAction == "previous") {
                question = it.next();
            }
            question = it.next();
            lastAction = "next";
            Logger.d(TAG, String.valueOf(question.index));
            current++;
            setQuestion(question);
            loadAnswers();
            if (checked) {
                checkAnswer();
            }
            //changeTextSize();
        }

    }

    public void currentQuestion() {
        //saveAnswers();
        if (question == null) {
            question = it.next();
            current++;
        }
        lastAction = "fist";
        setQuestion(question);
        loadAnswers();
        if (checked) {
             checkAnswer();
        }
    }

    public void saveAnswers() {
            //save answers
        if (linearLayoutContainer.getChildCount()>1) {
            answers.put(current, new Answer.Builder()
                    .setIndex(current)
                    .setChecked(checked)
                    .setAntwort("")
                    .setRichtig1(checkBox1.isChecked())
                    .setRichtig2(checkBox2.isChecked())
                    .setRichtig3(checkBox3.isChecked())
                    .setRichtig4(checkBox4.isChecked())
                    .setRichtig5(checkBox5.isChecked())
                    .build());
        } else if (linearLayoutContainer.getChildCount()==1) {
            answers.put(current, new Answer.Builder()
                    .setIndex(current)
                    .setChecked(checked)
                    .setAntwort(editText1.getText().toString())
                    .setRichtig1(false)
                    .setRichtig2(false)
                    .setRichtig3(false)
                    .setRichtig4(false)
                    .setRichtig5(false)
                    .build());
        }
            //answers.add(new Answer(current, editText_answer.getText().toString(), checkBox_answer1.isChecked(), checkBox_answer2.isChecked(), checkBox_answer3.isChecked(), checkBox_answer4.isChecked(), checkBox_answer5.isChecked() ));
    }

    //loads prevously answers
    public void loadAnswers() {
        checked = false;
        Answer answer = (Answer) answers.get(current);
        if (answer != null) {
            if ((question.type !=null) && (question.type.equals("auswahl"))) {
                checkBox1.setChecked(answer.richtig1);
                checkBox2.setChecked(answer.richtig2);
                checkBox3.setChecked(answer.richtig3);
                checkBox4.setChecked(answer.richtig4);
                checkBox5.setChecked(answer.richtig5);

            } else {
                editText1.setText(answer.antwort);
            }
            answered = false;
            checked = answer.checked;
            // if stored answer found and loaded
        }
    }

    //fills in the new question
    public void setQuestion(Question question) {
        clearView();
        addWidgets();
        textView_current.setText(Integer.toString(current)+"/"+Integer.toString(max));
//       textView_current.setText(it.nextIndex()-1+"/"+entries.size());

        textView_question.setText(question.text);

        if ((question.type != null) && (question.type.equals("auswahl"))) {

            checkBox1.setText(question.antwort1);
            checkBox2.setText(question.antwort2);
            checkBox3.setText(question.antwort3);
            checkBox4.setText(question.antwort4);
            checkBox5.setText(question.antwort5);
        }
    }

    //loads the answers from file and marks if correct
    public void checkAnswer() {
        checked = true;
        saveAnswers();
        if ((question.type !=null) && (question.type.equals("auswahl"))) {
            if ((question.richtig1 !=null) && question.richtig1) {
                if (checkBox1.isChecked() == true) {
                    checkBox1.setTextColor(Color.GREEN);
                } else {
                    checkBox1.setTextColor(Color.RED);
                }
            }
            if ((question.richtig2 !=null) && question.richtig2) {
                if (checkBox2.isChecked() == true) {
                    checkBox2.setTextColor(Color.GREEN);
                } else {
                    checkBox2.setTextColor(Color.RED);
                }
            }
            if ((question.richtig3 !=null) && question.richtig3) {
                if (checkBox3.isChecked() == true) {
                    checkBox3.setTextColor(Color.GREEN);
                } else {
                    checkBox3.setTextColor(Color.RED);
                }
            }
            if ((question.richtig4 !=null) && question.richtig4) {
                if (checkBox4.isChecked() == true) {
                    checkBox4.setTextColor(Color.GREEN);
                } else {
                    checkBox4.setTextColor(Color.RED);
                }
            }
            if ((question.richtig5 !=null) && question.richtig5) {
                if (checkBox5.isChecked() == true) {
                    checkBox5.setTextColor(Color.GREEN);
                } else {
                    checkBox5.setTextColor(Color.RED);
                }
            }
        } else {
            if (editText1.getText().toString().trim().equals(question.antwort1.trim())) {
                editText1.setTextColor(Color.GREEN);
            } else {
                editText1.setTextColor(Color.RED);
                editText1.setText(question.antwort1);
            }
        }
        //answered = false;
        //checked = true;

    }

    public void onEnd() {
        saveAnswers();
        Integer index = 0;
        Integer faults = 0;
        Integer points = 0;
        Integer maxpoints = 0;
        //ListIterator it = entries.subList(from, to).listIterator();
        ListIterator it = questions.listIterator();

        while (it.hasNext()) {
            Question question = (Question) it.next();
            index++;
            faults = 0;
            Answer answer = (Answer) answers.get(index);
            if (answer != null) {

                if ((question.type !=null) && (question.type.equals("auswahl"))) {
                    if ((question.richtig1 !=null) && (question.richtig1 != answer.richtig1 )) {
                        faults++;
                    }
                    if ((question.richtig2 !=null) && (question.richtig2 != answer.richtig2 )) {
                        faults++;
                    }
                    if ((question.richtig3 !=null) && (question.richtig3 != answer.richtig3 )) {
                        faults++;
                    }
                    if ((question.richtig4 !=null) && (question.richtig4 != answer.richtig4 )) {
                        faults++;
                    }
                    if ((question.richtig5 !=null) && (question.richtig5 != answer.richtig5 )) {
                        faults++;
                    }
                } else {
                    if (!question.antwort1.equals(answer.antwort)) {
                        faults++;
                    }
                }
                if (faults == 0) {
                    points = points + question.points;

                }

                answers.put(index, new Answer.Builder()
                        .setIndex(index)
                        .setChecked(true)
                        .setAntwort(answer.antwort)
                        .setRichtig1(answer.richtig1)
                        .setRichtig2(answer.richtig2)
                        .setRichtig3(answer.richtig3)
                        .setRichtig4(answer.richtig4)
                        .setRichtig5(answer.richtig5)
                        .build());

            } else {

                //set all to checked
                answers.put(index, new Answer.Builder()
                        .setIndex(index)
                        .setChecked(true)
                        .setAntwort("")
                        .setRichtig1(false)
                        .setRichtig2(false)
                        .setRichtig3(false)
                        .setRichtig4(false)
                        .setRichtig5(false)
                        .build());

            }
            maxpoints = maxpoints + question.points;
        }

        //although already checked, call to redraw the current question
        checkAnswer();

        Toast.makeText(getActivity(),
                "You reached " + points.toString() + " out of " + maxpoints.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the current article selection in case we need to recreate the fragment
        savedInstanceState.putInt(CURRENT, current);
        //savedInstanceState.putSerializable(ANSWERS, answers);
        //savedInstanceState.putSerializable(ENTRIES, entries);
        savedInstanceState.putSerializable(ANSWERS, answers);
        //savedInstanceState.putParcelableArrayList(ENTRIES, entries);
        savedInstanceState.putParcelableArrayList(QUESTIONS, questions);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        /*MainActivity activity = (MainActivity) getActivity();
        activity.getRefWatcher().watch(this);*/
    }
}



