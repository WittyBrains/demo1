package com.example.weatherforecast.activities;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.Utilities.DownloadImageTask;
import com.example.weatherforecast.Utilities.WeatherWebservice;
import com.tlenclos.weatherforecast.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TodayWeather extends Fragment implements TabListener, LocationListener {
    private static final String TAG = "TodayWeather";
    public static Weather dayWeather;
    public static Location location;
    private Fragment mFragment;
    private LocationManager locationManager;
    private TextView city;
    private TextView date;
    private TextView temperature;
    private TextView wind;
    private TextView humidity;
    private TextView description;
    private TextView time;
    private Button changeCityButton;
    private ImageView icon;
    private String zipcode;
    private static Boolean aBoolean=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.today_weather_tab);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        city = (TextView) getActivity().findViewById(R.id.city);
        date = (TextView) getActivity().findViewById(R.id.date);
        temperature = (TextView) getActivity().findViewById(R.id.temperature);
        wind = (TextView) getActivity().findViewById(R.id.wind);
        humidity = (TextView) getActivity().findViewById(R.id.humidity);
        icon = (ImageView) getActivity().findViewById(R.id.icon);
        description = (TextView) getActivity().findViewById(R.id.description);
        time = (TextView) getActivity().findViewById(R.id.time);

        Time currentTime = new Time();
        currentTime.setToNow();
        time.setText(String.format("%02d:%02d", currentTime.hour, currentTime.minute));

        changeCityButton = (Button) getActivity().findViewById(R.id.changecity);
        changeCityButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeCity.class);
                startActivity(intent);
                aBoolean=true;
            }
        });

        if(aBoolean == true)
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            zipcode = sharedPreferences.getString("editbox2",null);
            if(zipcode!=null) {
                gettingFreshWeatherData(null, true, zipcode);
            }else
                Toast.makeText(getActivity(),"Please select zipcode",Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }else{
            Toast.makeText(getActivity(),"please select city zipcode",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "Saving InstanceState..");
        if (dayWeather != null) {
            outState.putSerializable("dayWeather", dayWeather);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "Recovering data from instanceState");

        if (dayWeather != null) {
            updateUI(dayWeather);
        }
        if (savedInstanceState != null) {
            dayWeather = (Weather) savedInstanceState.getSerializable("dayWeather");
            updateUI(dayWeather);
        }
    }

    public void updateUI(Weather weather) {
        city.setText(weather.place);
        temperature.setText(String.format("%.1f�C", weather.temperature));
        wind.setText(weather.windSpeed + " km/h");
        humidity.setText(weather.humidity + "%");
        date.setText(new SimpleDateFormat("EEEE dd/MM", Locale.getDefault()).format(weather.day));
        description.setText(weather.description);

        if (weather.iconUri != null) {
            new DownloadImageTask(icon).execute(weather.iconUri);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.v(TAG, location.getLatitude() + " - " + location.getLongitude());
      //  locationManager.removeUpdates(this);
        // getting fresh weather data
       /* if (isAdded()) {
            gettingFreshWeatherData(location, true, null);
        }*/
    }

    private void gettingFreshWeatherData(Location location, boolean todayWeather, String city) {
        if (((MainActivity) getActivity()).isOnline()) {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fetching_data), Toast.LENGTH_SHORT).show();
            WeatherWebservice weatherWS = new WeatherWebservice(new FragmentCallback() {
                @Override
                public void onTaskDone(ArrayList<Weather> result) {
                    if (result.size() > 0 && result.get(0).isFetched) {
                        dayWeather = result.get(0);
                        updateUI(dayWeather);
                    }
                }
            }, location, todayWeather, city);
            weatherWS.execute();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        mFragment = this;
        ft.add(android.R.id.content, mFragment);
        ft.attach(mFragment);
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public interface FragmentCallback {
        public void onTaskDone(ArrayList<Weather> result);
    }

}