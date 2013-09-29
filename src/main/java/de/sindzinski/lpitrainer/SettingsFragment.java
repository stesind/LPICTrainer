package de.sindzinski.lpitrainer;


import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceFragment;
/**
 * Created by steffen on 15.08.13.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        //not already done in main activity
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
