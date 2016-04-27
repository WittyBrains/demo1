package com.example.weatherforecast.beans;

import android.location.Location;

public class UserInfo {
	public Location location;
	public Weather dayWeather;
	
	private UserInfo()
	{}
 
	private static UserInfo INSTANCE = new UserInfo();
 
	public static UserInfo getInstance()
	{	return INSTANCE;
	}
}
