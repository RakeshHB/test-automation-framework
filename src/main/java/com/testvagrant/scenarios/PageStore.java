package com.testvagrant.scenarios;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.sun.javafx.PlatformUtil;

@SuppressWarnings("restriction")
public class PageStore {

	WebDriver webDriver;
	List<Object> pages;

	public PageStore() {
		if (PlatformUtil.isWindows()) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
		} else if (PlatformUtil.isMac()) {
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		}
		webDriver = new ChromeDriver();
		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		pages = new ArrayList<Object>();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		for (Object page : pages) {
			if (page.getClass() == clazz) {
				return (T) page;
			}
		}
		T page = PageFactory.initElements(webDriver, clazz);
		pages.add(page);
		return page;
	}

	public void destroy() {
		pages.clear();
		webDriver.quit();
	}

	public WebDriver getDriver() {
		return webDriver;
	}
}
