package de.sindzinski.lpictrainer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.AsyncQueryHandler;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import de.sindzinski.lpictrainer.data.TrainerContract;
import de.sindzinski.lpictrainer.data.TrainerContract.AnswerEntry;
import de.sindzinski.util.HelpUtils;
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
        //setHasOptionsMenu(true);

        Bundle bundle = getIntent().getExtras();

        fileName = bundle.getString(EXTRA_FILENAME);
        from = bundle.getInt(EXTRA_FROM, 0);
        to = bundle.getInt(EXTRA_TO, 0);

//        //Load preferences
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//
//        boolean isDarkTheme = sharedPref.getBoolean("pref_key_theme", this.getResources().getBoolean(R.bool.pref_key_dark_default));
//        if (isDarkTheme) {
//            this.setTheme(android.R.style.Theme_DeviceDefault);
//        } else {
//            this.setTheme(android.R.style.Theme_DeviceDefault_Light);
//        }

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);

        mAdapter = new TestFragmentStatePagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        //mPager.setCurrentItem(1);

        // Watch for button clicks.
        ImageButton button = (ImageButton) findViewById(R.id.button_back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PagerTestFragment fragment = (PagerTestFragment) mAdapter.instantiateItem(null, mPager.getCurrentItem());
                fragment.saveAnswer();
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        button = (ImageButton) findViewById(R.id.button_forward);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PagerTestFragment fragment = (PagerTestFragment) mAdapter.instantiateItem(null, mPager.getCurrentItem());
                fragment.saveAnswer();
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });
        button = (ImageButton) findViewById(R.id.button_check);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PagerTestFragment fragment = (PagerTestFragment) mAdapter.instantiateItem(null, mPager.getCurrentItem());
//                PagerTestFragment fragment = (PagerTestFragment) mAdapter.getFragment(mPager.getCurrentItem());
                fragment.markAnswer();
                fragment.saveAnswer();

            }
        });

    }

    public class TestFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public TestFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return to-from;
        }

        @Override
        public Fragment getItem(int current) {
            return PagerTestFragment.newInstance(current + 1, from, to);
        }
//        @SuppressWarnings("unchecked")
//        public Fragment getFragment(int position) {
//            try {
//                Field f = FragmentStatePagerAdapter.class.getDeclaredField("mFragments");
//                f.setAccessible(true);
//                ArrayList<Fragment> fragments = (ArrayList<Fragment>) f.get(this);
//                if (fragments.size() > position) {
//                    return fragments.get(position);
//                }
//                return null;
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }

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
                AnswerEntry._ID,
                AnswerEntry.COLUMN_ID,
//                AnswerEntry.COLUMN_CHECKED,
                AnswerEntry.COLUMN_POINTS,
        };
        //String selectionClause = TrainerContract.AnswerEntry.CO + "=" + mAnswer.index;
        String selectionClause = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        try {
            Cursor cursor = this.getContentResolver().query(
                    AnswerEntry.CONTENT_URI,
                    projection,
                    selectionClause,
                    selectionArgs,
                    sortOrder);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        //points = points + 1;
                        int rowPoints = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS)));
                        if (rowPoints > 0 ) {
                            points = points + 1;
                                    }

                    } while (cursor.moveToNext());
                }
                // always close the cursor
                cursor.close();
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "Error reading database: " + e);
        }
        maxPoints = to - from;
        Toast.makeText(this,
                "You reached " + points.toString() + " out of " + maxPoints.toString(), Toast.LENGTH_LONG).show();

//        int token = 2;
//        AsyncQueryHandler handler =
//                new AsyncQueryHandler(this.getContentResolver()) {
//                    @Override
//                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//                        super.onQueryComplete(token, cookie, cursor);
//                        if (cursor != null) {
//                            Integer points =0;
//                            Integer maxPoints = 0;
//                            if (cursor.moveToFirst()) {
//                                do {
//                                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS))) > 0 ) {
//                                        points = points + 1;
//                                    }
//                                    //points = points + cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_POINTS);
//                                } while (cursor.moveToNext());
//                            }
//                            // always close the cursor
//                            cursor.close();
//                            maxPoints = to - from;
//                            Toast.makeText(TestActivity.this,
//                                    "You reached "
//                                            + points.toString()
//                                            + " out of "
//                                            + maxPoints.toString(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                };
//
//        handler.startQuery(token,
//                null,
//                AnswerEntry.CONTENT_URI,
//                projection,
//                selectionClause,
//                selectionArgs,
//                sortOrder);
    }
}

