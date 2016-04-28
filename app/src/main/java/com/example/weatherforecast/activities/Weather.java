package com.example.weatherforecast.activities;

import java.io.Serializable;
import java.util.Date;

public class Weather implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7468907373314597663L;
	
	public String place;
	public double temperature;
	public double humidity;
	public double windSpeed;
	public int pressure;
	public String iconUri;
	public String description;
	public Date day;
	public boolean isFetched;
	
	public Weather() {
		// Default values
		day = new Date();
		temperature = humidity = windSpeed = pressure = 0;
		isFetched = false;
	}
}
