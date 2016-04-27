package com.example.weatherforecast.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;

import com.example.weatherforecast.activities.HomeInfoTab.FragmentCallback;
import com.example.weatherforecast.beans.UserInfo;
import com.example.weatherforecast.beans.Weather;

public class WeatherWebservice extends AsyncTask<Void, Void, ArrayList<Weather>> {
	private FragmentCallback mFragmentCallback;
	String apiUrlIcon = "http://openweathermap.org/img/w/";
	String apiUrlFormat = "http://api.openweathermap.org/data/2.1/find/city?&lat=%f&lon=%f&cnt=1&APPID=5d2eef1e303470228dcf653b4f989499";
	String apiForecastUrlFormat = "http://api.openweathermap.org/data/2.5/forecast/daily?&lat=%f&lon=%f&APPID=5d2eef1e303470228dcf653b4f989499";
	String apiSearchCityFormat = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=5d2eef1e303470228dcf653b4f989499";
	String apiUrl;
	Location location;
	boolean todayWeather;
	String citySearch;
	
	public WeatherWebservice(FragmentCallback fragmentCallback, Location location, boolean todayWeather, String citySearch) {
		mFragmentCallback = fragmentCallback;
		this.todayWeather = todayWeather;
		this.citySearch = citySearch;
		
		if (location != null) {
			this.location = UserInfo.getInstance().location = location;
		}
		
		if (citySearch != null) {
			this.apiUrl = String.format(apiSearchCityFormat.toString(), citySearch);
		} else {
			this.apiUrl = String.format(
					todayWeather ? apiUrlFormat.toString() : apiForecastUrlFormat.toString(),
					location.getLatitude(),
					location.getLongitude()
			);
		}
	}
	
	@Override
	protected ArrayList<Weather> doInBackground(Void... arg0) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(apiUrl);
		StringBuilder builder = null;
		
		// Get Response
		try {
			HttpResponse response = httpClient.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
        }
		
		String response = builder.toString();
		
		return todayWeather ? parseTodayWeather(response) : parseWeekWeather(response);
	}
	
	private ArrayList<Weather> parseTodayWeather(String response) {
		Weather dayWeather = new Weather();
		
		// Parse JSON
		JSONObject weatherData = null;
		JSONArray list = null;
		
		try {
			if (citySearch != null) {
				weatherData = new JSONObject(response);
			} else {
				list = new JSONObject(response).getJSONArray("list");
				if (list.length() > 0) {
					weatherData = list.getJSONObject(0);
				}
			}

			if (weatherData != null) {
				dayWeather.place = weatherData.getString("name");
				dayWeather.temperature = kelvinToCelsius(weatherData.getJSONObject("main").getDouble("temp"));
				dayWeather.humidity = weatherData.getJSONObject("main").getDouble("humidity");
				dayWeather.pressure = weatherData.getJSONObject("main").getInt("pressure");
				dayWeather.windSpeed = weatherData.getJSONObject("wind").getDouble("speed");
				dayWeather.description = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
				dayWeather.iconUri = apiUrlIcon+weatherData.getJSONArray("weather").getJSONObject(0).getString("icon")+".png";
				dayWeather.isFetched = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Weather> weathers = new ArrayList<Weather>();
		weathers.add(dayWeather);
		return weathers;
	}
	
	private ArrayList<Weather> parseWeekWeather(String response) {
		// Parse JSON
		JSONObject jsonObject = null;
		JSONArray list = null;
		ArrayList<Weather> weathers = new ArrayList<Weather>();
		
		try {
			jsonObject = new JSONObject(response);
			list = jsonObject.getJSONArray("list");
			
			for(int i=0; i < list.length(); i++) {
				Weather dayWeather = new Weather();
				JSONObject weatherData = list.getJSONObject(i);
				dayWeather.temperature = kelvinToCelsius(weatherData.getJSONObject("temp").getDouble("day"));
				dayWeather.humidity = weatherData.getDouble("humidity");
				dayWeather.pressure = weatherData.getInt("pressure");
				dayWeather.windSpeed = weatherData.getDouble("speed");
				
				final Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(weatherData.getLong("dt") * 1000);
				dayWeather.day = cal.getTime();
				
				dayWeather.iconUri = apiUrlIcon+weatherData.getJSONArray("weather").getJSONObject(0).getString("icon")+".png";
				dayWeather.description = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
				dayWeather.isFetched = true;

				weathers.add(dayWeather);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return weathers;
	}
	
	public double kelvinToCelsius(double kelvin) {
		return kelvin - 273.15;
	}
	
	protected void onPostExecute(ArrayList<Weather> result) {
		mFragmentCallback.onTaskDone(result);
	}
}
