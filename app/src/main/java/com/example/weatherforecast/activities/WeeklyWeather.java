package com.example.weatherforecast.activities;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weatherforecast.Utilities.WeatherWebservice;
import com.example.weatherforecast.adapters.CustomListAdapter;
import com.tlenclos.weatherforecast.R;

import java.util.ArrayList;

public class WeeklyWeather extends Fragment implements TabListener {
    private static final String TAG = "AppWeather";
    static ArrayList<Weather> weathers;
    private Fragment mFragment;
    private ListView daysListView;
    private CustomListAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.week_weather_tab);

        weathers = new ArrayList<Weather>();
        daysListView = (ListView) this.getActivity().findViewById(R.id.days_list);
        adapter = new CustomListAdapter(this.getActivity(), weathers);
        daysListView.setAdapter(adapter);
        
        // Get weather data
        if (TodayWeather.location != null) {
            if (((MainActivity) this.getActivity()).isOnline()) {
                Toast.makeText(this.getActivity().getApplicationContext(), getResources().getString(R.string.fetching_data), Toast.LENGTH_SHORT).show();

                WeatherWebservice weatherWS = new WeatherWebservice(new TodayWeather.FragmentCallback() {
                    @Override
                    public void onTaskDone(ArrayList<Weather> result) {
    	                weathers.clear();
    	                weathers.addAll(result);
    	                adapter.notifyDataSetChanged();
    	            }
                }, TodayWeather.location, false, null);
                weatherWS.execute();
            } else {
    			Toast.makeText(this.getActivity().getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    		}
        } else {
        	Toast.makeText(this.getActivity().getApplicationContext(), getResources().getString(R.string.no_location), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "Recovering data from instanceState");
        
        // TODO : Use a clean way
        if (weathers != null) {
        	adapter.notifyDataSetChanged();
        }
    }
    
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        mFragment = this;
        // Attach fragment1.xml layout
        ft.add(android.R.id.content, mFragment);
        ft.attach(mFragment);
    }
 
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        // Remove fragment1.xml layout
        ft.remove(mFragment);
    }
 
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
 
    }
 
}