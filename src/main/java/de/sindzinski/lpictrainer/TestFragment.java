package de.sindzinski.lpictrainer;

/**
 * Created by steffen on 17.08.13.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.preference.PreferenceManager;
import java.util.Collections;
import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.Keyframe;
import android.view.GestureDetector;

public class TestFragment extends Fragment {

    public ArrayList<Entry> entries = null;
    //public ArrayList <Answer> answers = null;
    public HashMap answers = new HashMap();
    public String fileName;
    public Integer from;
    public Integer to;
    public ListIterator<Entry> it;
    protected TextView textView_question;
    protected CheckBox checkBox_answer1;
    protected CheckBox checkBox_answer2;
    protected CheckBox checkBox_answer3;
    protected CheckBox checkBox_answer4;
    protected CheckBox checkBox_answer5;
    protected EditText editText_answer;
    protected TextView textView_solution;
    protected TextView textView_current;
    protected ScrollView scrollView;
    protected LinearLayout linearLayout;
    protected RelativeLayout relativeLayout;
    protected ActivitySwipeDetector swipe;
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
    protected Entry entry = null;

    protected Boolean answered = false;
    protected Boolean checked = false;

    private static final String TAG="LPITrainer";
    private static final String CURRENT="CURRENT_QUESTION";
    private static final String ANSWERS="CURRENT_ANSWERS";
    private static final String ENTRIES="CURRENT_ENTRIES";

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private boolean shuffle = false;
    private int textSize = 2;
    private int textSizeResource;

    private int mTouchSlop;
    private int mMinimumFlingVelocity;
/*
    private GestureDetector gestureScanner;


    public boolean onTouchEvent(MotionEvent me)
    {
        return gestureScanner.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.i(TAG, "LEFTERS <<");


            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.i(TAG, "rIGTHERS >>");

            }
            else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            }
        } catch (Exception e) {
            // nothing
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }
*/

    //later get the arguments with:
    //getArguments().getInt("someInt", 0);
    public static TestFragment newInstance(Integer from, Integer to, String fileName) {
        TestFragment f = new TestFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("from", from);
        args.putInt("to", to);
        args.putString("fileName", fileName);
        f.setArguments(args);

        return f;
    }

    //needed for adding items to the menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //setRetainInstance(true); //savedInstancestate will always be null!!!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get Arguments
        from = getArguments().getInt("from", 0);
        to = getArguments().getInt("to", 0);
        fileName = getArguments().getString("fileName");

        //only valid settings
        if (from > to) {
            from = 0;
        }

        //get default preferences from preferencemanager not from shared settings
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        shuffle = sharedPref.getBoolean("pref_key_shuffle", getActivity().getResources().getBoolean(R.bool.pref_key_shuffle_default));
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

        //not already done in main activity
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

/*      //unused spinner
        //beim erzeugen des ArrayAdapters nicht von der activity sondern actionBar.getThemedContext() ableiten damit die Hintergrundfarbe richtig ist
        SpinnerAdapter mSpinnerAdapter;
        mSpinnerAdapter = ArrayAdapter.createFromResource(actionBar.getThemedContext(), R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.

        OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                Toast.makeText(getActivity(), strings[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        //alternativ kann der Listener auch im fragment mit "implements OnNavigationListener implementiert werden
        //dann kann die funktion auch im fragment gemacht werden
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener );*/

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

/*
        //another swipe solution using extra class
        ActivitySwipeDetectorLR swipe = new ActivitySwipeDetectorLR(getActivity(),new SwipeInterfaceLR() {
            @Override
            public void onLeftToRight(View v) {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
                prevQuestion();
            }

            @Override
            public void onRightToLeft(View v) {
                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
                nextQuestion();
            }
        });
        ScrollView swipe_layout = (ScrollView) view.findViewById(R.id.scrollView);
        swipe_layout.setOnTouchListener(swipe);
*/


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
                        Log.i(TAG, "onDown has been called!");
                        mLastOnDownEvent = e;
                        //return super.onDown(e);
                        return true;
                    }
                    /* onScroll gives several events on one scroll, so it cannot be used for a page flip

                     */
/*                    @Override
                    public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        //Log.i(TAG, "onScroll has been called!");
                        if (e1==null) {
                            Log.i(TAG, "onScroll - e1 = null" + " e2 = " + e2.getX() + "  distance X " + distanceX + " distance Y " + distanceY);
                        } else {
                            Log.i(TAG, "onScroll - e1 = " + e1.getX() + " e2 = " + e2.getX() + "  distance X " + distanceX + " distance Y " + distanceY);
                        }

                            Log.i(TAG, "horizontal scroll");
                            if (distanceX > 0) {
                                Log.i(TAG, "right to left");
                                nextQuestion();
                            } else {
                                Log.i(TAG, "left to right");

                                prevQuestion();
                            }

                        return true;
                    }*/

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i(TAG, "onFling has been called!");
                        if (e1==null) {
                            Log.i(TAG, "onFling - e1 = null e2 = " + e2.getX() + "velocityX: " + velocityX);
                        } else {
                            Log.i(TAG, "onFling - e1 = " + e1.getX() + " e2 = " + e2.getX() + "  distance X " + (e1.getX() - e2.getX()) + "velocityX: " + velocityX);
                        }


                        //final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MIN_DISTANCE = mTouchSlop;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        //final int SWIPE_THRESHOLD_VELOCITY = 200;
                        //final int SWIPE_THRESHOLD_VELOCITY = 0;
                        final int SWIPE_THRESHOLD_VELOCITY = mMinimumFlingVelocity;


                        try {
/*                           if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                                Log.i(TAG, "Off path");
                                return false;
                            }*/
/*                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Right to Left" + (e1.getX() - e2.getX()));
                                nextQuestion();
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Left to Right" + (e2.getX() - e1.getX()));
                                prevQuestion();
                            }*/
                            /*unfortunately sometimes e1 is null, so the distance cannot calculated
                            maybe it has something to do with onInterceptTouchEvent ACTION_DOWN but if it is not
                            returning false then buttons do not work anymore

                             */
                            if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                if (velocityX > 0) {
                                    Log.i(TAG, "Left to Right" );
                                    prevQuestion();
                                } else {
                                    Log.i(TAG, "Right to Left" );
                                    nextQuestion();
                                }
                            }
                        } catch (Exception e) {
                            // nothing
                            //Log.i(TAG, "Exception" + e.getMessage().toString());
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



        if (savedInstanceState != null) {
            //redrawn
            current = savedInstanceState.getInt(CURRENT,0);
            answers = (HashMap) savedInstanceState.getSerializable(ANSWERS);
            entries = (ArrayList) savedInstanceState.getSerializable(ENTRIES);
        } else {
            //newly drawn
            //load first data to display
            try {
                //load from sqlite database
                DatabaseHandler db = new DatabaseHandler(getActivity());
                entries = (ArrayList) db.getAllEntries();

                //if set in preferences then shuffle entries
                if (shuffle) {
                    Collections.shuffle(entries);
                }

            } catch (SQLiteException e) {
                Log.e(TAG, "Error reading database: " + e);
            }
        }

        //entries = loadXmlFromFile();
        //check if from is <= to
        it = entries.subList((from > to) ? 0 : from, to).listIterator();

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
        nextQuestion();

        changeTextSize();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
/*        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }*/

        //scroll to the current question
        //updateTest(current);
    }



    private void setupAnimations(LayoutTransition transition) {
        // Changing while Adding
        PropertyValuesHolder pvhLeft =
                PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop =
                PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight =
                PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom =
                PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhScaleX =
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY =
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).
                setDuration(transition.getDuration(LayoutTransition.CHANGE_APPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        changeIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation =
                PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).
                setDuration(transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
        changeOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotation(0f);
            }
        });

        // Adding
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).
                setDuration(transition.getDuration(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // Removing
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).
                setDuration(transition.getDuration(LayoutTransition.DISAPPEARING));
        transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });

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
        if ((entry.type !=null) && (entry.type.equals("auswahl"))) {
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

    public void addEditText() {
        EditText editText1 = new EditText(getActivity());
        //frameLayout.setBackgroundColor(Color.TRANSPARENT);
        editText1.setText("test");
        linearLayoutContainer.addView(editText1);
    }

    public void clearView() {
        linearLayoutContainer.removeAllViews();
    }

    public void prevQuestion() {
        saveAnswers();
        if (it.hasPrevious()) {
            if (lastAction == "next") {
                entry = it.previous();
            }
            entry = it.previous();
            lastAction = "previous";
            current--;
            setQuestion(entry);
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
                entry = it.next();
            }
            entry = it.next();
            lastAction = "next";
            current++;
            setQuestion(entry);
            loadAnswers();
            if (checked) {
                checkAnswer();
            }
            //changeTextSize();
        }

    }

    public void saveAnswers() {
            //save answers
        if (linearLayoutContainer.getChildCount()>1) {
            answers.put(current, new Answer(current,checked, "", checkBox1.isChecked(), checkBox2.isChecked(), checkBox3.isChecked(), checkBox4.isChecked(), checkBox5.isChecked()));
        } else if (linearLayoutContainer.getChildCount()==1) {
            answers.put(current, new Answer(current,checked, editText1.getText().toString(), false, false, false, false, false));
        }
            //answers.add(new Answer(current, editText_answer.getText().toString(), checkBox_answer1.isChecked(), checkBox_answer2.isChecked(), checkBox_answer3.isChecked(), checkBox_answer4.isChecked(), checkBox_answer5.isChecked() ));
    }

    //loads prevously answers
    public void loadAnswers() {
        checked = false;
        Answer answer = (Answer) answers.get(current);
        if (answer != null) {
            if ((entry.type !=null) && (entry.type.equals("auswahl"))) {
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
    public void setQuestion(Entry entry) {
        clearView();
        addWidgets();
        textView_current.setText(Integer.toString(current)+"/"+Integer.toString(max));
//       textView_current.setText(it.nextIndex()-1+"/"+entries.size());

        textView_question.setText(entry.text);

        if ((entry.type != null) && (entry.type.equals("auswahl"))) {

            checkBox1.setText(entry.antwort1);
            checkBox2.setText(entry.antwort2);
            checkBox3.setText(entry.antwort3);
            checkBox4.setText(entry.antwort4);
            checkBox5.setText(entry.antwort5);
        }
    }

    //loads the answers from file and marks if correct
    public void checkAnswer() {
        checked = true;
        saveAnswers();
        if ((entry.type !=null) && (entry.type.equals("auswahl"))) {
            if ((entry.richtig1 !=null) && entry.richtig1) {
                if (checkBox1.isChecked() == true) {
                    checkBox1.setTextColor(Color.GREEN);
                } else {
                    checkBox1.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig2 !=null) && entry.richtig2) {
                if (checkBox2.isChecked() == true) {
                    checkBox2.setTextColor(Color.GREEN);
                } else {
                    checkBox2.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig3 !=null) && entry.richtig3) {
                if (checkBox3.isChecked() == true) {
                    checkBox3.setTextColor(Color.GREEN);
                } else {
                    checkBox3.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig4 !=null) && entry.richtig4) {
                if (checkBox4.isChecked() == true) {
                    checkBox4.setTextColor(Color.GREEN);
                } else {
                    checkBox4.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig5 !=null) && entry.richtig5) {
                if (checkBox5.isChecked() == true) {
                    checkBox5.setTextColor(Color.GREEN);
                } else {
                    checkBox5.setTextColor(Color.RED);
                };
            }
        } else {
            if (editText1.getText().toString().trim().equals(entry.antwort1.trim())) {
                editText1.setTextColor(Color.GREEN);
            } else {
                editText1.setTextColor(Color.RED);
                editText1.setText(entry.antwort1);
            };
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
        ListIterator it = entries.subList(from, to).listIterator();

        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            index++;
            faults = 0;
            Answer answer = (Answer) answers.get(index);
            if (answer != null) {

                if ((entry.type !=null) && (entry.type.equals("auswahl"))) {
                    if ((entry.richtig1 !=null) && (entry.richtig1 != answer.richtig1 )) {
                        faults++;
                    }
                    if ((entry.richtig2 !=null) && (entry.richtig2 != answer.richtig2 )) {
                        faults++;
                    }
                    if ((entry.richtig3 !=null) && (entry.richtig3 != answer.richtig3 )) {
                        faults++;
                    }
                    if ((entry.richtig4 !=null) && (entry.richtig4 != answer.richtig4 )) {
                        faults++;
                    }
                    if ((entry.richtig5 !=null) && (entry.richtig5 != answer.richtig5 )) {
                        faults++;
                    }
                } else {
                    if (!entry.antwort1.equals(answer.antwort)) {
                        faults++;
                    };
                }
                if (faults == 0) {
                    points = points + entry.points;

                }

                answers.put(index, new Answer(index,true, answer.antwort, answer.richtig1, answer.richtig2, answer.richtig3, answer.richtig4, answer.richtig5 ));

            } else {

                //set all to checked
                answers.put(index, new Answer(index,true, "", false, false, false, false, false ));

            }
            maxpoints = maxpoints + entry.points;
        }

        //although already checked, call to redraw the current question
        checkAnswer();

        Toast.makeText(getActivity(),
                "You reached " + points.toString() + " out of " + maxpoints.toString(), Toast.LENGTH_LONG).show();
    }

    //reads xml from file
    //
    private ArrayList <Entry> loadXmlFromFile() throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        XmlParser parser = new XmlParser();
        ArrayList <Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(fileName);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            entries = parser.parse(dis);
            return entries;

        }

        finally {
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
    }

/*    public static class Answer implements Serializable {

        public Integer index = 0;
        public boolean checked;
        public String antwort;
        public boolean richtig1;
        public boolean richtig2;
        public boolean richtig3;
        public boolean richtig4;
        public boolean richtig5;

        private Answer(Integer index, Boolean checked, String antwort, Boolean richtig1, Boolean richtig2, Boolean richtig3, Boolean richtig4, Boolean richtig5) {
            this.index = index;
            this.checked = checked;
            this.antwort = antwort;
            this.richtig1 = richtig1;
            this.richtig2 = richtig2;
            this.richtig3 = richtig3;
            this.richtig4 = richtig4;
            this.richtig5 = richtig5;
        }
    }*/
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the current article selection in case we need to recreate the fragment
        savedInstanceState.putInt(CURRENT, current);
        //savedInstanceState.putSerializable(ANSWERS, answers);
        //savedInstanceState.putSerializable(ENTRIES, entries);
        savedInstanceState.putSerializable(ANSWERS, answers);
        savedInstanceState.putParcelableArrayList(ENTRIES, entries);
    }



    public class MyLayout extends LinearLayout {
        public MyLayout(Context context) {
            super(context);

        }
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            // do whatever you want with the event
            // and return true so that children don't receive it
            return true;
        }
}
}


