package de.sindzinski.lpictrainer;

import android.content.AsyncQueryHandler;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import de.sindzinski.lpictrainer.data.TrainerContract;
import de.sindzinski.lpictrainer.data.TrainerContract.AnswerEntry;
import de.sindzinski.util.Logger;

/**
 * Created by steffen on 22.02.16.
 */
public class TestFragment extends Fragment {

    public static final int loaderID = 1;
    private final static String EXTRA_FILENAME = "de.sindzinski.lpictrainer.FILENAME";
    private final static String EXTRA_FROM = "de.sindzinski.lpictrainer.FROM";
    private final static String EXTRA_TO = "de.sindzinski.lpictrainer.TO";
    private final static String EXTRA_CURRENT = "de.sindzinski.lpictrainer.CURRENT";
    private final String TAG = "TestFragment";
    public int mCurrent;
    public int mNumItems;
    public TestFragmentStatePagerAdapter mAdapter;
    ViewPager mPager;
    List<Integer> questionList;
    ListIterator<Integer> questionIterator;
    private String fileName;
    private Integer from;
    private Integer to;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        //Bundle bundle = getIntent().getExtras();

        fileName = bundle.getString(EXTRA_FILENAME);
        from = bundle.getInt(EXTRA_FROM, 0);
        to = bundle.getInt(EXTRA_TO, 0);

        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.test_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

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

        mAdapter = new TestFragmentStatePagerAdapter(getActivity().getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(mAdapter);

        //mPager.setCurrentItem(1);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.button_check);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //PagerTestFragment fragment = (PagerTestFragment) mAdapter.instantiateItem(null, mPager.getCurrentItem());
                PagerTestFragment fragment = (PagerTestFragment) mAdapter.getCurrentFragment();
                fragment.markAnswer();
                fragment.saveAnswer();

            }
        });

        //async implementation
        int token = 3;
        AsyncQueryHandler handler =
                new AsyncQueryHandler(getActivity().getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                        super.onQueryComplete(token, cookie, cursor);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    questionList.add(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ID))));
                                    // shuffle if needed
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                                    boolean shuffle = sharedPref.getBoolean("pref_key_shuffle", getResources().getBoolean(R.bool.pref_key_shuffle_default));
                                    if (shuffle) {
                                        Collections.shuffle(questionList);
                                    }
                                } while (cursor.moveToNext());
                            }
                            // always close the cursor
                            cursor.close();
                        }
                    }

                    @Override
                    protected void onDeleteComplete(int token, Object cookie, int result) {
                        super.onDeleteComplete(token, cookie, result);
                        Logger.i(TAG, "Answer Rows deleted: " + result);
                    }
                };

        // get list of questions

        questionList = new ArrayList();

        Uri uri = Uri.parse(TrainerContract.QuestionEntry.CONTENT_URI + "/"
                + from + "/" + to);
        String[] projection = {
                TrainerContract.QuestionEntry.COLUMN_ID,
        };
        //String selectionClause = TrainerContract.AnswerEntry.CO + "=" + mAnswer.index;
        String selectionClause = null;
        String[] selectionArgs = null;
        String sortOrder = null;

//        handler.startQuery(token,
//                null,
//                uri,
//                projection,
//                selectionClause,
//                selectionArgs,
//                sortOrder);

//        //run to current item
//        int i = 1;

//        while (i < current) {
//            it.next();
//            i++;
//        }
//        max = to-from;
//        if (current > 0) {
//            current--; //neccessary because current is increased in nextQuestion
//        }

        //async delete of old answers
//        String mSelection = null;
//        String[] mSelectionArgs = null;
//        long rowsDeleted = getContentResolver().delete(TrainerContract.AnswerEntry.CONTENT_URI, mSelection, mSelectionArgs);
//        handler.startDelete(token,
//                null,
//                uri,
//                selectionClause,
//                selectionArgs
//        );

        try {
            Cursor cursor = getActivity().getContentResolver().query(
                    uri,
                    projection,
                    selectionClause,
                    selectionArgs,
                    sortOrder);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Integer result = 0;
                    do {
                        //points = points + 1;
                        result = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TrainerContract.QuestionEntry.COLUMN_ID)));
                        questionList.add(result);
                        Logger.d(TAG, "added to questionList: " + result.toString());
                    } while (cursor.moveToNext());
                }
                // always close the cursor
                cursor.close();
            }

            // shuffle if needed
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean shuffle = sharedPref.getBoolean("pref_key_shuffle", getResources().getBoolean(R.bool.pref_key_shuffle_default));
            if (shuffle) {
                Collections.shuffle(questionList);
                Logger.d(TAG, "shuffling question list");
            }

        } catch (SQLiteException e) {
            Logger.e(TAG, "Error reading database: " + e);
        }
//    delete all old
        String mSelection = null;
        String[] mSelectionArgs = null;
        long rowsDeleted = getActivity().getContentResolver().delete(AnswerEntry.CONTENT_URI, mSelection, mSelectionArgs);
        Logger.i(TAG, "Old answer rows deleted: " + rowsDeleted);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }


    public class TestFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public TestFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        private Fragment mCurrentFragment;

        @Override
        public int getCount() {
            return to - from;
        }

        @Override
        public Fragment getItem(int current) {
            return PagerTestFragment.newInstance(questionList.get(current), from, to, current + 1);
        }
        @Override
        public CharSequence getPageTitle(int current) {
            //return Integer.toString(current);
            return ((questionList != null) ? questionList.get(current).toString() : "0");
        }


        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }
//        //@SuppressWarnings("unchecked")
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
}

