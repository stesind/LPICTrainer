package de.sindzinski.lpitrainer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TestActivity extends Activity  {

    public ArrayList <Entry> entries = null;
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
    protected TextView textView_current;
    protected ScrollView scrollView;
    protected LinearLayout linearLayout;
    protected ActivitySwipeDetector swipe;

    public Integer current = 0;
    public String lastAction = null;
    public Integer max = 0;
    protected Entry entry = null;

    protected Boolean answered = false;
    protected Boolean checked = false;

    private static final String TAG="LPITrainer";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //enable the back button in action bar


        // Get the message from the intent
        Intent intent = getIntent();
        fileName = intent.getStringExtra(MainActivity.EXTRA_FILENAME);
        from = intent.getIntExtra(MainActivity.EXTRA_FROM,0);
        to = intent.getIntExtra(MainActivity.EXTRA_TO, 120);

        textView_current = (TextView) findViewById(R.id.textView_current);
        textView_question = (TextView) findViewById(R.id.textView_question);
        checkBox_answer1 = (CheckBox) findViewById(R.id.checkBox_answer1);
        checkBox_answer2 = (CheckBox) findViewById(R.id.checkBox_answer2);
        checkBox_answer3 = (CheckBox) findViewById(R.id.checkBox_answer3);
        checkBox_answer4 = (CheckBox) findViewById(R.id.checkBox_answer4);
        checkBox_answer5 = (CheckBox) findViewById(R.id.checkBox_answer5);
        editText_answer = (EditText) findViewById(R.id.editText_answer);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        checkBox_answer1.setOnCheckedChangeListener(new OnCheckedChangeListener()
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
        checkBox_answer2.setOnCheckedChangeListener(new OnCheckedChangeListener()
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
        checkBox_answer3.setOnCheckedChangeListener(new OnCheckedChangeListener()
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
        checkBox_answer4.setOnCheckedChangeListener(new OnCheckedChangeListener()
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
        checkBox_answer5.setOnCheckedChangeListener(new OnCheckedChangeListener()
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

        try {
            //load from sqlite database
            DatabaseHandler db = new DatabaseHandler(this);
            entries = (ArrayList) db.getAllEntries();

            //entries = loadXmlFromFile();
            it = entries.subList(from, to).listIterator();

            max = to-from;
            nextQuestion();
        } catch (SQLiteException e) {
            Log.e(TAG, "Error reading database: " + e);
        }

        ActivitySwipeDetectorLR swipe = new ActivitySwipeDetectorLR(this,new SwipeInterfaceLR() {
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
        ScrollView swipe_layout = (ScrollView) findViewById(R.id.scrollView);
        swipe_layout.setOnTouchListener(swipe);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch (item.getItemId()) {
            case R.menu.main:
                // Handle Settings
                return true;
            case R.id.about:
                //startActivity(new Intent(this, About.class));
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set title
                alertDialogBuilder.setTitle("About");

                // set dialog message
                alertDialogBuilder
                        .setMessage(R.string.copyright)
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                dialog.dismiss();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            case R.id.menu_legalnotices:
                String LicenseInfo = null;
                AssetManager am = this.getAssets();
                try {
                    InputStream is = am.open("License");
                    LicenseInfo = convertStreamToString(is);
                }
                catch (IOException e) {
                    Log.e(TAG, "Error reading file: " + e);
                }

                AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(this);
                LicenseDialog.setTitle("Legal Notices");
                LicenseDialog.setMessage(LicenseInfo);
                LicenseDialog.show();
                return true;
            /*case R.id.help:
                //startActivity(new Intent(this, Help.class));
                return true;*/
            case R.id.end:
                //startActivity(new Intent(this, Help.class));
                onEnd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearView() {
        textView_question.setText("");
        checkBox_answer1.setText("");
        checkBox_answer2.setText("");
        checkBox_answer3.setText("");
        checkBox_answer4.setText("");
        checkBox_answer5.setText("");
        checkBox_answer1.setChecked(false);
        checkBox_answer2.setChecked(false);
        checkBox_answer3.setChecked(false);
        checkBox_answer4.setChecked(false);
        checkBox_answer5.setChecked(false);
        checkBox_answer1.setTextColor(Color.BLACK);
        checkBox_answer2.setTextColor(Color.BLACK);
        checkBox_answer3.setTextColor(Color.BLACK);
        checkBox_answer4.setTextColor(Color.BLACK);
        checkBox_answer5.setTextColor(Color.BLACK);
        checkBox_answer1.setVisibility(View.VISIBLE);
        checkBox_answer2.setVisibility(View.VISIBLE);
        checkBox_answer3.setVisibility(View.VISIBLE);
        checkBox_answer4.setVisibility(View.VISIBLE);
        checkBox_answer5.setVisibility(View.VISIBLE);
        editText_answer.setText("");
        editText_answer.setVisibility(View.INVISIBLE);
    }

    public void onClickBack(View view) {
        prevQuestion();
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
            checked = false;
            loadAnswers();
            if (checked == true) {
                setAnswer();
            }
        }
    }
    public void onClickForwd(View view) {
        nextQuestion();
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
            checked = false;
            loadAnswers();
            if (checked == true) {
                setAnswer();
            }
        }
    }

    public void saveAnswers() {
        if (answered | checked) {
            //save answers
            answers.put(current, new Answer(current,checked, editText_answer.getText().toString(), checkBox_answer1.isChecked(), checkBox_answer2.isChecked(), checkBox_answer3.isChecked(), checkBox_answer4.isChecked(), checkBox_answer5.isChecked() ));

            //answers.add(new Answer(current, editText_answer.getText().toString(), checkBox_answer1.isChecked(), checkBox_answer2.isChecked(), checkBox_answer3.isChecked(), checkBox_answer4.isChecked(), checkBox_answer5.isChecked() ));
            answered = false;
            checked = false;
        }
    }

    //loads prevously answers
    public void loadAnswers() {
        Answer answer = (Answer) answers.get(current);
        if (answer != null) {
                if ((entry.type !=null) && (entry.type.equals("auswahl"))) {
                    checkBox_answer1.setChecked(answer.richtig1);
                    checkBox_answer2.setChecked(answer.richtig2);
                    checkBox_answer3.setChecked(answer.richtig3);
                    checkBox_answer4.setChecked(answer.richtig4);
                    checkBox_answer5.setChecked(answer.richtig5);

                } else {
                    editText_answer.setText(answer.antwort);
                }
                answered = false;
                checked = answer.checked;

            }
        }

    public void setQuestion(Entry entry) {
        clearView();
        textView_current.setText(Integer.toString(current)+"/"+Integer.toString(max));
//       textView_current.setText(it.nextIndex()-1+"/"+entries.size());
        textView_question.setText(entry.text);
        if ((entry.type != null) && (entry.type.equals("auswahl"))) {
            checkBox_answer1.setText(entry.antwort1);
            checkBox_answer2.setText(entry.antwort2);
            checkBox_answer3.setText(entry.antwort3);
            checkBox_answer4.setText(entry.antwort4);
            checkBox_answer5.setText(entry.antwort5);
        } else {
            checkBox_answer1.setVisibility(View.INVISIBLE);
            checkBox_answer2.setVisibility(View.INVISIBLE);
            checkBox_answer3.setVisibility(View.INVISIBLE);
            checkBox_answer4.setVisibility(View.INVISIBLE);
            checkBox_answer5.setVisibility(View.INVISIBLE);
            editText_answer.setVisibility(View.VISIBLE);
        }
    }

    public void onClickCheck(View view) {
        setAnswer();
    }
    //loads the answers from file
    public void setAnswer() {
        if ((entry.type !=null) && (entry.type.equals("auswahl"))) {
            if ((entry.richtig1 !=null) && entry.richtig1) {
                if (checkBox_answer1.isChecked() == true) {
                    checkBox_answer1.setTextColor(Color.GREEN);
                } else {
                    checkBox_answer1.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig2 !=null) && entry.richtig2) {
               if (checkBox_answer2.isChecked() == true) {
                    checkBox_answer2.setTextColor(Color.GREEN);
                } else {
                    checkBox_answer2.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig3 !=null) && entry.richtig3) {
               if (checkBox_answer3.isChecked() == true) {
                    checkBox_answer3.setTextColor(Color.GREEN);
                } else {
                    checkBox_answer3.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig4 !=null) && entry.richtig4) {
               if (checkBox_answer4.isChecked() == true) {
                    checkBox_answer4.setTextColor(Color.GREEN);
                } else {
                    checkBox_answer4.setTextColor(Color.RED);
                };
            }
            if ((entry.richtig5 !=null) && entry.richtig5) {
               if (checkBox_answer5.isChecked() == true) {
                    checkBox_answer5.setTextColor(Color.GREEN);
                } else {
                    checkBox_answer5.setTextColor(Color.RED);
                };
            }
        } else {
            editText_answer.setText(entry.antwort1);
        }
        answered = false;
        checked = true;

    }

    public void onButtonEnd(View view) {
        onEnd();
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

        Toast.makeText(getApplicationContext(),
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
}
