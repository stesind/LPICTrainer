package de.sindzinski.lpictrainer;

/**
 * Created by steffen on 12.12.16.
 */

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.ListIterator;
import de.sindzinski.lpictrainer.TestFragment.TestFragmentStatePagerAdapter;

import de.sindzinski.lpictrainer.data.TrainerContract;


public class TestActivity extends AppCompatActivity {
    public static final int loaderID = 1;
    private final static String EXTRA_FILENAME = "de.sindzinski.lpictrainer.FILENAME";
    private final static String EXTRA_FROM = "de.sindzinski.lpictrainer.FROM";
    private final static String EXTRA_TO = "de.sindzinski.lpictrainer.TO";
    private final static String EXTRA_CURRENT = "de.sindzinski.lpictrainer.CURRENT";
    private final String TAG = "TestActivity";
    public int mCurrent;
    public int mNumItems;
    TestFragmentStatePagerAdapter mAdapter;
    ViewPager mPager;
    List<Integer> questionList;
    ListIterator<Integer> questionIterator;
    private String fileName;
    private Integer from;
    private Integer to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.test_toolbar);
        setSupportActionBar(myToolbar);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        Bundle bundle = getIntent().getExtras();

        fileName = bundle.getString(EXTRA_FILENAME);
        from = bundle.getInt(EXTRA_FROM, 0);
        to = bundle.getInt(EXTRA_TO, 0);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putString(EXTRA_FILENAME, fileName);
            arguments.putInt(EXTRA_FROM, from);
            arguments.putInt(EXTRA_TO, to);

            TestFragment fragment = new TestFragment();
            fragment.setArguments(arguments);

            //arguments.putParcelable(TestFragment.DETAIL_URI, getIntent().getData());
            //fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.test_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test, menu);
        //inflater.inflate(R.menu.zoom, menu);
        //MenuItem end = menu.add("@string/menu_end");
        //end.setIcon(R.drawable.ic_menu_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.help:
                //startActivity(new Intent(this, Help.class));
                return true;*/
            case R.id.end:
                //startActivity(new Intent(this, Help.class));
                onCheckAll();
                return true;
            case R.id.zoom:
                //startActivity(new Intent(this, Help.class));
                //Toast.makeText(getActivity(), "zoom", Toast.LENGTH_LONG).show();
                //nextTextSize();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onCheckAll() {
        Integer points = 0;
        Integer maxPoints = 0;

        //Uri mDataUrl = Uri.parse(TrainerContract.AnswerEntry.CONTENT_URI + "/"
        //        + mAnswer.index);
        String[] projection = {
                TrainerContract.AnswerEntry._ID,
                TrainerContract.AnswerEntry.COLUMN_ID,
//                AnswerEntry.COLUMN_CHECKED,
                TrainerContract.AnswerEntry.COLUMN_POINTS,
        };
        //String selectionClause = TrainerContract.AnswerEntry.CO + "=" + mAnswer.index;
        String selectionClause = null;
        String[] selectionArgs = null;
        String sortOrder = null;

//        try {
//            Cursor cursor = this.getContentResolver().query(
//                    AnswerEntry.CONTENT_URI,
//                    projection,
//                    selectionClause,
//                    selectionArgs,
//                    sortOrder);
//
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        //points = points + 1;
//                        int rowPoints = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS)));
//                        if (rowPoints > 0 ) {
//                            points = points + 1;
//                                    }
//
//                    } while (cursor.moveToNext());
//                }
//                // always close the cursor
//                cursor.close();
//            }
//        } catch (SQLiteException e) {
//            Logger.e(TAG, "Error reading database: " + e);
//        }
//        maxPoints = to - from;
//        Toast.makeText(this,
//                "You reached " + points.toString() + " out of " + maxPoints.toString(), Toast.LENGTH_LONG).show();

        int token = 2;
        AsyncQueryHandler handler =
                new AsyncQueryHandler(this.getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                        super.onQueryComplete(token, cookie, cursor);
                        if (cursor != null) {
                            Integer points = 0;
                            Integer maxPoints = 0;
                            if (cursor.moveToFirst()) {
                                do {
                                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS))) > 0) {
                                        points = points + 1;
                                    }
                                    //points = points + cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS);
                                } while (cursor.moveToNext());
                            }
                            // always close the cursor
                            cursor.close();
                            maxPoints = to - from;
                            Toast.makeText(de.sindzinski.lpictrainer.TestActivity.this,
                                    "You reached "
                                            + points.toString()
                                            + " out of "
                                            + maxPoints.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

        handler.startQuery(token,
                null,
                TrainerContract.AnswerEntry.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                sortOrder);
    }

}