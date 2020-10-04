package com.testvagrant.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class LandingPage {

	WebDriver driver;

	@FindBy(xpath = "//a[@href='javascript:void(0);'][@class='m-nv_lnk']")
	private WebElement menu;

	@FindBy(xpath = "//a[@href='https://social.ndtv.com/static/Weather/report/'][@class='m-nv_lnk'][@title='WEATHER']")
	private WebElement weatherSection;

	public LandingPage(WebDriver driver) {
		this.driver = driver;
	}

	public void hoverOnMenu() {
		Actions actions = new Actions(driver);
		actions.moveToElement(menu).perform();
	}

	public void goToWeatherReportPage() {
		weatherSection.click();
	}
}
