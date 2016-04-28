package com.example.weatherforecast.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.tlenclos.weatherforecast.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab dailyWeatherTab = actionBar.newTab().setTabListener(new TodayWeather());
        dailyWeatherTab.setText(getResources().getString(R.string.tab_today));
        actionBar.addTab(dailyWeatherTab);

        Tab weeklyWeatherTab = actionBar.newTab().setTabListener(new WeeklyWeather());
        weeklyWeatherTab.setText(getResources().getString(R.string.tab_week));
        actionBar.addTab(weeklyWeatherTab);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null && network.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}