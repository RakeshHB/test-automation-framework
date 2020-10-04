package com.testvagrant.domain;

public class CityDetailsBuilder {
	private String cityName;
	private Number windSpeedInMeterPerSecond;
	private Number humidity;
	private Number tempInKelvin;

	public CityDetailsBuilder withCityName(String cityName) {
		this.cityName = cityName;
		return this;
	}


	public CityDetailsBuilder withWindSpeedInMeterPerSecond(Number windSpeedInMeterPerSecond) {
		this.windSpeedInMeterPerSecond = windSpeedInMeterPerSecond;
		return this;
	}


	public CityDetailsBuilder withHumidity(Number humidity) {
		this.humidity = humidity;
		return this;
	}

	public CityDetailsBuilder withTempInKelvin(Number tempInKelvin) {
		this.tempInKelvin = tempInKelvin;
		return this;
	}

	public CityDetails build() {
		return new CityDetails(cityName, windSpeedInMeterPerSecond, humidity,
				tempInKelvin);
	}

}
