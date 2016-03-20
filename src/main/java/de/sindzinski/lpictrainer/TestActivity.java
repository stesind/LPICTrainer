package de.sindzinski.lpictrainer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
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

import de.sindzinski.util.HelpUtils;

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
//            this.setTheme(android.R.style.Theme_Holo);
//        } else {
//            this.setTheme(android.R.style.Theme_Holo_Light);
//        }

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);

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
        button = (ImageButton) findViewById(R.id.button_check);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PagerTestFragment fragment = (PagerTestFragment) mAdapter.instantiateItem(null, mPager.getCurrentItem());
//                PagerTestFragment fragment = (PagerTestFragment) mAdapter.getFragment(mPager.getCurrentItem());
                fragment.checkAnswer();

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
        for (int i = 1; i <= to; i++) {
            //mPager.findViewById()
            PagerTestFragment fragment = (PagerTestFragment) mAdapter.instantiateItem(null, i);
            if (fragment != null) {
//                PagerTestFragment fragment = (PagerTestFragment) mAdapter.getFragment(mPager.getCurrentItem());
                Bundle bundle = fragment.check();
                points = points + bundle.getInt("points");
                maxPoints = maxPoints + bundle.getInt("maxPoints");
            }
        }
        Toast.makeText(this,
                "You reached " + points.toString() + " out of " + maxPoints.toString(), Toast.LENGTH_LONG).show();
    }

}

