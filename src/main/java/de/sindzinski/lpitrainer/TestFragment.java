package de.sindzinski.lpitrainer;

/**
 * Created by steffen on 17.08.13.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;

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
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.app.ActionBar.OnNavigationListener;

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

    protected Integer current = 0;
    public String lastAction = null;
    public Integer max = 0;
    protected Entry entry = null;

    protected Boolean answered = false;
    protected Boolean checked = false;

    private static final String TAG="LPITrainer";
    private static final String CURRENT="CURRENT_QUESTION";
    private static final String ANSWERS="CURRENT_ANSWERS";

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

        if (savedInstanceState != null) {
            current = savedInstanceState.getInt(CURRENT,0);
            answers = (HashMap) savedInstanceState.getSerializable(ANSWERS);
        }

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

        //animation of layout changes
        //can also be done in xml layout by android:animateLayoutChanges="trtransaction.commit();ue"
//        LayoutTransition layoutTransition = new LayoutTransition();
        //required min api level 16!
//        linearLayoutContainer.setLayoutTransition(layoutTransition);

        buttonBack = (ImageButton) view.findViewById(R.id.button_back);
        buttonCheck = (ImageButton) view.findViewById(R.id.button_check);
        buttonForward = (ImageButton) view.findViewById(R.id.button_forward);

        /*checkBox_answer1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    // perform logic
                    answered = true;
                }

            }
        });
        checkBox_answer2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    // perform logic
                    answered = true;
                }

            }
        });
        checkBox_answer3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    // perform logic
                    answered = true;
                }

            }
        });
        checkBox_answer4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    // perform logic
                    answered = true;
                }

            }
        });
        checkBox_answer5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    // perform logic
                    answered = true;
                }

            }
        });*/

        ActivitySwipeDetectorLR swipe = new ActivitySwipeDetectorLR(getActivity(),new SwipeInterfaceLR() {
            @Override
            public void onLeftToRight(View v) {
                //Toast.makeText(TestActivity.this, "left", Toast.LENGTH_SHORT).show();
                prevQuestion();
            }

            @Override
            public void onRightToLeft(View v) {
                //Toast.makeText(TestActivity.this, "right", Toast.LENGTH_SHORT).show();
                nextQuestion();
            }
        });
        ScrollView swipe_layout = (ScrollView) view.findViewById(R.id.scrollView);
        swipe_layout.setOnTouchListener(swipe);

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

        //load first data to display
        try {
            //load from sqlite database
            DatabaseHandler db = new DatabaseHandler(getActivity());
            entries = (ArrayList) db.getAllEntries();

            //entries = loadXmlFromFile();
            //check if from is <= to
            it = entries.subList((from > to) ? 0 : from, to).listIterator();

            //run to current item
            int i = 1;
            if (current > 0) {
                current--; //neccessary because current is increased in nextQuestion
            }
            while (i < current) {
                it.next();
                i++;
            }
            max = to-from;
            nextQuestion();
        } catch (SQLiteException e) {
            Log.e(TAG, "Error reading database: " + e);
        }

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
                Toast.makeText(getActivity(), "zoom", Toast.LENGTH_LONG).show();
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
            linearLayoutContainer.addView(checkBox1);
            checkBox2 = new CheckBox(getActivity());
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox2.setText("test");
            linearLayoutContainer.addView(checkBox2);
            checkBox3 = new CheckBox(getActivity());
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox3.setText("test");
            linearLayoutContainer.addView(checkBox3);
            checkBox4 = new CheckBox(getActivity());
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox4.setText("test");
            linearLayoutContainer.addView(checkBox4);
            checkBox5 = new CheckBox(getActivity());
            //frameLayout.setBackgroundColor(Color.TRANSPARENT);
            checkBox5.setText("test");
            linearLayoutContainer.addView(checkBox5);

        } else {
            editText1 = new EditText(getActivity());
            linearLayoutContainer.addView(editText1);
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

    public static class Answer {

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the current article selection in case we need to recreate the fragment
        savedInstanceState.putInt(CURRENT, current);
        savedInstanceState.putSerializable(ANSWERS, answers);
    }

}

