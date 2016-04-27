package com.example.weatherforecast.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
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
import com.tlenclos.weatherforecast.R;
import com.example.weatherforecast.Utilities.WeatherWebservice;
import com.example.weatherforecast.beans.Weather;
 
public class HomeInfoTab extends Fragment implements TabListener, LocationListener {
	private static final String TAG = "AppWeather";
    private Fragment mFragment;
	private LocationManager locationManager;
	private TextView city;
	private TextView date;
	private TextView temperature;
	private TextView wind;
	private TextView humidity;
	private TextView description;
	private TextView time;
	private Button changecity;
	private ImageView icon;
	static Weather dayWeather;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from fragment1.xml
        getActivity().setContentView(R.layout.home_tab);
        
     	// Geoloc user
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        
        city = (TextView) getActivity().findViewById(R.id.city);
    	date = (TextView) getActivity().findViewById(R.id.date);
    	temperature = (TextView) getActivity().findViewById(R.id.temperature);
    	wind = (TextView) getActivity().findViewById(R.id.wind);
    	humidity = (TextView) getActivity().findViewById(R.id.humidity);
    	time = (TextView) getActivity().findViewById(R.id.time);
    	icon = (ImageView) getActivity().findViewById(R.id.icon);
    	changecity = (Button) getActivity().findViewById(R.id.changecity);
    	description = (TextView) getActivity().findViewById(R.id.description);
    			
    	// Time
    	Time now = new Time();
    	now.setToNow();
    	time.setText(String.format("%02d:%02d", now.hour, now.minute));
    	
    	// Change city action
    	changecity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				askToChangeCity();
			}
		});
    }
    
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "Saving instanceState");
        
        if (dayWeather != null) {
        	outState.putSerializable("dayWeather", dayWeather);
        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "Recovering data from instanceState");
        
        // TODO : Use a clean way
        if (dayWeather != null) {
        	updateUIwithWeather(dayWeather);
        }
        
        // Not working
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            dayWeather = (Weather) savedInstanceState.getSerializable("dayWeather");
            updateUIwithWeather(dayWeather);
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
   
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.v(TAG, location.getLatitude() + " - " + location.getLongitude());
		locationManager.removeUpdates(this);
		
		// Get weather data
		if (isAdded()) {
			if ( ((MainActivity) this.getActivity()).isOnline() ) {
				Toast.makeText(this.getActivity().getApplicationContext(), getResources().getString(R.string.fetching_data), Toast.LENGTH_SHORT).show();
				
				WeatherWebservice weatherWS = new WeatherWebservice(new FragmentCallback() {
		            @Override
		            public void onTaskDone(ArrayList<Weather> result) {
		                // Update UI
		        		if (result.size() > 0 && result.get(0).isFetched) {
		        			dayWeather = result.get(0);
		        			// Update UI
		        			updateUIwithWeather(dayWeather);
		        		}
		            }
		        }, location, true, null);
				weatherWS.execute();
			} else {
				Toast.makeText(this.getActivity().getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void updateUIwithWeather(Weather weather) {
		city.setText(weather.place);
		temperature.setText(String.format("%.1f�C", weather.temperature));
		wind.setText(weather.windSpeed+"km/h");
		humidity.setText(weather.humidity+"%");
		date.setText(new SimpleDateFormat("EEEE dd/MM", Locale.getDefault()).format(weather.day));
		description.setText(weather.description);
		
		if (weather.iconUri != null)
			new DownloadImageTask(icon).execute(weather.iconUri);
	}
 
	public void askToChangeCity() {
	  	AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());

    	alert.setTitle(getResources().getString(R.string.alert_search_title));
    	alert.setMessage(getResources().getString(R.string.alert_search_message));

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(this.getActivity());
    	alert.setView(input);

    	alert.setPositiveButton(getResources().getString(R.string.alert_search_validate), new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			Editable value = input.getText();
    			
    			// Get weather data
    			if (((MainActivity) getActivity()).isOnline()) {
    				Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fetching_data), Toast.LENGTH_SHORT).show();
    				
    				WeatherWebservice weatherWS = new WeatherWebservice(new FragmentCallback() {
    		            @Override
    		            public void onTaskDone(ArrayList<Weather> result) {
    		                // Update UI
    		        		if (result.size() > 0 && result.get(0).isFetched) {
    		        			dayWeather = result.get(0);
    		        			// Update UI
    		        			updateUIwithWeather(dayWeather);
    		        		}
    		            }
    		        }, null, true, value.toString());
    				weatherWS.execute();
    			} else {
    				Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    			}
    		}
    	});

    	alert.setNegativeButton(getResources().getString(R.string.alert_search_cancel), new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    	    // Canceled.
    	  }
    	});

    	alert.show();
	}
	
    public interface FragmentCallback {
        public void onTaskDone(ArrayList<Weather> result);
    }

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}