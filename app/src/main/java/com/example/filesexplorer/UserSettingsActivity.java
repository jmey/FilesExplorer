package com.example.filesexplorer;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class UserSettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
