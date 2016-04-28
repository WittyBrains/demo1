package com.example.weatherforecast.Utilities;

import android.location.Location;
import android.os.AsyncTask;

import com.example.weatherforecast.activities.TodayWeather.FragmentCallback;
import com.example.weatherforecast.beans.Weather;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class WeatherWebservice extends AsyncTask<Void, Void, ArrayList<Weather>> {
    private FragmentCallback mFragmentCallback;
    private String iconUrl = "http://openweathermap.org/img/w/";
    private String apiUrlFormat = "http://api.openweathermap.org/data/2.1/find/city?&lat=%f&lon=%f&cnt=1" +
            "&APPID=5d2eef1e303470228dcf653b4f989499";
    private String apiForecastUrlFormat = "http://api.openweathermap.org/data/2.5/forecast/daily?&lat=%f&lon=%f&" +
            "APPID=5d2eef1e303470228dcf653b4f989499";
    private String apiSearchCityFormat = "http://api.openweathermap.org/data/2.5/weather?q=%s&" +
            "APPID=5d2eef1e303470228dcf653b4f989499";
    private String apiUrl;
    protected Location location;
    private boolean todayWeather;
    private String city;

    public WeatherWebservice(FragmentCallback fragmentCallback, Location location, boolean todayWeather, String city) {
        this.mFragmentCallback = fragmentCallback;
        this.todayWeather = todayWeather;
        this.city = city;

        if (location != null) {
            this.location = location;
        }
        if (city != null) {
            this.apiUrl = String.format(apiSearchCityFormat.toString(), city);
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
        try {
            HttpResponse response = httpClient.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = builder.toString();
        return todayWeather ? parseTodayWeather(response) : parseWeekWeather(response);
    }

    private ArrayList<Weather> parseTodayWeather(String response) {
        ArrayList<Weather> weathers = new ArrayList<Weather>();
        Weather weather = new Weather();
        JSONObject jsonObject = null;
        JSONArray list = null;
        try {
            if (city != null) {
                jsonObject = new JSONObject(response);
            } else {
                list = new JSONObject(response).getJSONArray("list");
                if (list.length() > 0) {
                    jsonObject = list.getJSONObject(0);
                }
            }
            if (jsonObject != null) {
                weather.place = jsonObject.getString("name");
                weather.temperature = kelvinToCelsius(jsonObject.getJSONObject("main").getDouble("temp"));
                weather.humidity = jsonObject.getJSONObject("main").getDouble("humidity");
                weather.pressure = jsonObject.getJSONObject("main").getInt("pressure");
                weather.windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
                weather.description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                weather.iconUri = iconUrl + jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png";
                weather.isFetched = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        weathers.add(weather);
        return weathers;
    }

    private ArrayList<Weather> parseWeekWeather(String response) {
        ArrayList<Weather> weathers = new ArrayList<Weather>();
        JSONObject jsonObject = null;
        JSONArray list = null;
        try {
            jsonObject = new JSONObject(response);
            list = jsonObject.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject weatherData = list.getJSONObject(i);
                Weather weather = new Weather();

                weather.temperature = kelvinToCelsius(weatherData.getJSONObject("temp").getDouble("day"));
                weather.humidity = weatherData.getDouble("humidity");
                weather.pressure = weatherData.getInt("pressure");
                weather.windSpeed = weatherData.getDouble("speed");
                final Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(weatherData.getLong("dt") * 1000);
                weather.day = cal.getTime();
                weather.iconUri = iconUrl + weatherData.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png";
                weather.description = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
                weather.isFetched = true;

                weathers.add(weather);
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
