package de.sindzinski.lpictrainer;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

/**
 * Created by steffen on 01.12.15.
 */
//public class MainActivityTest extends TestCase {
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity MainActivityTest;
    //private TextView mFirstTestText;

    //test class constructor
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MainActivityTest = getActivity();
        //mFirstTestText = (TextView) MainActivityTest.findViewById(R.id.my_first_test_text_view);
    }

    public void testPreconditions() {
        assertNotNull("MainActivity is null", MainActivityTest);
        //assertNotNull(“mFirstTestText is null”, mFirstTestText);
    }

    public void testMyFirstTestTextView_labelText() {
        //final String expected =
        //        mFirstTestActivity.getString(R.string.my_first_test);
        //final String actual = mFirstTestText.getText().toString();
        //assertEquals(expected, actual);
    }
}