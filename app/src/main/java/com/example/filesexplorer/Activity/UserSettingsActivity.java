package com.example.filesexplorer.Activity;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.filesexplorer.R;

import java.io.File;

public class UserSettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Preference myPref = (Preference) findPreference("pref_clear_prefs_file");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                File prefFile = new File("/data/data/" + getPackageName() + "/shared_prefs");
                for (File f : prefFile.listFiles()) {
                    f.delete();
                }
                UserSettingsActivity.this.finish();
                return true;
            }
        });
    }
}
