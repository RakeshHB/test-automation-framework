package com.testvagrant.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.testvagrant.domain.CityDetails;
import com.testvagrant.domain.CityDetailsBuilder;

public class SearchResultsPage {

	WebDriver driver;

	public SearchResultsPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean resultsAppearForSelectedCity(String city) {
		driver.findElement(By.xpath("//div[@class='cityText'][text()='" + city + "']")).click();
		By by = By.xpath("//div[@class='leaflet-popup-content']");
		if(isElementPresent(by)) {
			storeWeatherInfo(by);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean resultsAppearForSelectedCity(CityDetails cityDetails) {
		return resultsAppearForSelectedCity(cityDetails.getCity());
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElements(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void storeWeatherInfo(By by) {
		List<WebElement> details = driver.findElements(by);
		String webElementText = details.get(0).getText();
		Number windSpeedInMeterPerSecond = null;
		Number humidityNumber = null;
		Number tempInKelvin = null;
		String[] infoArray = webElementText.split("\n");
		for (String info : infoArray) {
			if (info.contains("Wind:")) {
				String[] winds = info.split(":");
				winds = winds[1].split(" KPH Gusting to ");
				String windSpeedInKPH = winds[0].trim();
				double windSpeedInKiloMeterPerHour = Double.parseDouble(windSpeedInKPH);
				windSpeedInMeterPerSecond = windSpeedInKiloMeterPerHour / 3.6;
			} else if (info.contains("Humidity:")) {
				String[] humidity = info.split(":");
				humidity = humidity[1].trim().split("%");
				humidityNumber =  Double.parseDouble(humidity[0]);
			} else if (info.contains("Temp in Degrees:")) {
				String tempInCelcius = info.split(":")[1].trim();
				double tempInDegreeCelcius = Double.parseDouble(tempInCelcius);
				tempInKelvin = tempInDegreeCelcius + 273.15;
			}
		}
		new CityDetailsBuilder().withWindSpeedInMeterPerSecond(windSpeedInMeterPerSecond).withTempInKelvin(tempInKelvin).withHumidity(humidityNumber).build();
	}
}
