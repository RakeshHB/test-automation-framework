package com.testvagrant.domain;

//add comments
public class CityDetails {
	private static String cityName;
	private static Number windSpeedInMeterPerSecond;
	private static Number humidity;
	private static Number tempInKelvin;

	public CityDetails(String cityName, Number windSpeedInMeterPerSecond,
			Number humidity, Number tempInKelvin) {
		CityDetails.cityName = cityName;
		CityDetails.windSpeedInMeterPerSecond = windSpeedInMeterPerSecond;
		CityDetails.humidity = humidity;
		CityDetails.tempInKelvin = tempInKelvin;
	}

	public String getCity() {
		return cityName;
	}


	public static Number getWindSpeedInMeterPerSecond() {
		return windSpeedInMeterPerSecond;
	}


	public static Number getHumidity() {
		return humidity;
	}

	public static Number getTempInKelvin() {
		return tempInKelvin;
	}

}
