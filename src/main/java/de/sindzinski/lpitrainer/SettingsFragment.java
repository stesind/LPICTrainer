package de.sindzinski.lpitrainer;


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
    }

}
