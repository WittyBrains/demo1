package com.example.weatherforecast.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.tlenclos.weatherforecast.R;

/**
 * Created by Wittybrains on 5/3/2016.
 */
public class ChangeCity extends PreferenceActivity {
    String temp;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    /*    Preference checkboxPreference1 = findPreference("checkbox1");
        Preference checkboxPreference2 = findPreference("checkbox2");
        
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removePreference(checkboxPreference1);
        preferenceScreen.removePreference(checkboxPreference2);*/

       /* PreferenceScreen preferenceScreen = getPreferenceScreen();

        preferenceScreen.removePreference((ListPreference) findPreference("list"));*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
               // finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
