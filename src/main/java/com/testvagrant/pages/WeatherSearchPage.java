package com.testvagrant.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.testvagrant.domain.CityDetails;

public class WeatherSearchPage {
	WebDriver driver;

	@FindBy(xpath = "//div[@class='leaflet-popup-content']")
	private WebElement weatherDetailsPopUp;

	@FindBy(xpath = "//input[@type='text'][@class='searchBox'][@id='searchBox']")
	private WebElement cityName;

	public WeatherSearchPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean selectCity(String city) {
		cityName.sendKeys(city);
		WebElement cityCheckBox = driver.findElement(By.xpath("//input[@id='" + city + "'][@type='checkbox']"));
		if(!cityCheckBox.isSelected()) {
			cityCheckBox.click();
		}
		By by = By.xpath("//div[@class='outerContainer'][@title='" + city + "']");
		if(isElementPresent(by)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean searchesForACityWeatherWith(CityDetails cityDetails) {
		return this.selectCity(cityDetails.getCity());
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
