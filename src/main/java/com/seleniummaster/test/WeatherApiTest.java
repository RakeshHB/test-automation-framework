package com.seleniummaster.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONObject;

public class WeatherApiTest {
	private WebDriver driver;
	private String baseUrl;
//	static Logger log = Logger.getLogger(WeatherApiTest.class);

	@BeforeTest
	public void setUp() throws Exception {
		System.setProperty("log4j.configurationFile", "log4j2.xml");
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
//		log.setLevel(Level.INFO);
//		log.info("This is logger info");
		System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
//		baseUrl = "https://www.ndtv.com/";
//		couldn't find waether section in above url. Hence using below url and
//		commented
//		above url. Will remove this comment and the below url once I start to see
//		weather
//		section in above url and start using the above url
		baseUrl = "https://www.ndtv.com/india-news/mumbai-rain-heavy-rain-in-city-and-neighbouring-areas-met-office-warns-of-more-rain-on-thursday-2278400";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterTest
	public void tearDown() throws Exception {
		driver.close();
		driver.quit();
	}

	@Test
	public void test() throws ClientProtocolException, IOException {
		driver.get(baseUrl);
		WebElement webElement = driver.findElement(By.xpath("//a[@href='javascript:void(0);'][@class='m-nv_lnk']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(webElement).perform();
//		WebElement weather = driver.findElement(By.linkText("WEATHER"));
//		if(weather.getAttribute("href").equals("https://social.ndtv.com/static/Weather/report/"))
//		weather.click();
//		handle alerts
		WebElement weather1 = driver.findElement(By.xpath(
				"//a[@href='https://social.ndtv.com/static/Weather/report/'][@class='m-nv_lnk'][@title='WEATHER']"));
		weather1.click();
//		WebElement city = driver.findElement(By.xpath("//input[@type='text'][@class='searchBox'][@id='searchBox']"));
//		city.click();
//		city.sendKeys("Bengaluru");
//		WebElement dropdown = driver.findElement(By.xpath("//div[@class='messages'][@id='messages']"));
//		List<WebElement> dropdown = driver.findElements(By.id("messages"));
//		List<WebElement> dropdown = driver.findElements(By.xpath("//div[@id='messages']/div"));
//		for (WebElement webElement2 : dropdown) {
//			if (webElement2.getText().contains("Bengaluru")) {
//				if (!webElement2.isSelected())
//					webElement2.click();
//			}
//		}
//		WebElement dropdown = driver.findElement(By.xpath("//div[@id='messages']/div"));
//		Select citySelect = new Select(dropdown);
//		citySelect.selectByVisibleText("Bengaluru");
//		citySelect.deselectByValue("Bengaluru");
//		WebElement reset = driver.findElement(By.xpath("//span[@id='icon_holder'][@title='Reset to default']"));
//		reset.click();
//		List<WebElement> dropdown = driver.findElements(By.xpath("//div[@id='messages']/div"));
//		for (WebElement webElement2 : dropdown) {
//			if (webElement2.getText().contains("Bengaluru")) {
//					webElement2.click();
//			}
//		}
//		contains(
		String cityName = "Bengaluru";
		WebElement city1 = driver.findElement(By.xpath("//div[@class='cityText'][text()='" + cityName + "']"));
		city1.click();
		List<WebElement> details = driver.findElements(By.xpath("//div[@class='leaflet-popup-content']"));
		Map<String, String> hashMap = new LinkedHashMap<String, String>();
		hashMap = storeWeatherInfo(details.get(0).getText(), hashMap);
		System.out.println("Parsed Info from UI Object:");
		System.out.println("===========================");
		System.out.println(hashMap);
		String mapFinalString = hashMap.toString();
		for (int i = 0; i < mapFinalString.length(); i++) {
			System.out.print("=");
		}
		System.out.println();
		WeatherApiResponse weatherApiResponse = new WeatherApiResponse();
		String expectedString = weatherApiResponse.getResponse();
		System.out.println(expectedString);

		for (int i = 0; i < expectedString.length(); i++) {
			System.out.print("=");
		}
		System.out.println();
		Map<String, String> apiMap = new LinkedHashMap<String, String>();
		Map<String, String> keyPathPair = new LinkedHashMap<String, String>();
		// multi weather id support -> v2 release
		keyPathPair.put("city", "$.name");
//		keyPathPair.put("weatherCondition", "$.weather[0].id");
		keyPathPair.put("windSpeedInMeterPerSecond", "$.wind.speed");
		keyPathPair.put("humidity", "$.main.humidity");
		keyPathPair.put("tempInKelvin", "$.main.temp");
		apiMap = storeAPIInfo(expectedString, apiMap, keyPathPair);
		System.out.println("Parsed Info from API Object:");
		System.out.println("============================");
		System.out.println(apiMap);
		// allow different ranges for different values
		//modularity
		String allowedDifference = "0.3";
		boolean isTestPassed = true;
		Set<String> apiParsedKeys = apiMap.keySet();
		for (Iterator<String> iterator = apiParsedKeys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object o1 = hashMap.get(key);
			Object o2 = apiMap.get(key);
			double diff = compare(o1, o2, allowedDifference);
			// handle string here and add tests
			//add logger
			if (diff != 0.0) {
				isTestPassed = false;
				System.out.println("Marking this test as failed as " + key + " has value " + o1
						+ " got/derived from UI but has value " + o2
						+ " from API and the difference between both values is " + diff
						+ " which is greather than allowed difference by "
						+ (diff - Double.parseDouble(allowedDifference))
						+ " unit which is not allowed. Allowed difference " + allowedDifference);
			}
		}
		Assert.assertTrue(isTestPassed);
	}

	private double compare(Object o1, Object o2, String allowedDifference) {
		double allowedDiff = Double.parseDouble(allowedDifference);
		double diff = 0.0;
		try {
			double value1 = Double.parseDouble(o1.toString());
			double value2 = Double.parseDouble(o2.toString());
			diff = Math.abs(value1 - value2);
			if (diff <= allowedDiff) {
				return 0;
			}
		} catch (Exception e) {
			if (o1.equals(o2)) {
				return 0;
			}
		}
		return diff;
	}

	private Map<String, String> storeAPIInfo(String expectedString, Map<String, String> apiMap,
			Map<String, String> keyPathPair) {

		Set<String> keys = keyPathPair.keySet();
		for (String key : keys) {
			String path = keyPathPair.get(key);
			String value = null;
			try {
				value = JsonPath.parse(expectedString).read(path, JSONObject.class).toString();
			} catch (ClassCastException e) {
				value = JsonPath.parse(expectedString).read(path, String.class);
			}
//			if ("weatherCondition".equals(key)) {
//				Properties properties = new Properties();
//				try {
//					properties.load(new FileReader("weatherconditioncodes.properties"));
//					value = properties.getProperty(value);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			apiMap.put(key, value);
		}
		return apiMap;
	}

	private Map<String, String> storeWeatherInfo(String webElementText, Map<String, String> hashMap) {
		// create a file constants and take values from them
		System.out.println("UI Object:");
		System.out.println("==========");
		System.out.println(webElementText);
		System.out.println("================================");
		String[] infoArray = webElementText.split("\n");
		for (String info : infoArray) {
			if (info.contains(",")) {
				hashMap.put("city", (info.split(","))[0]);
			} else if (info.contains("Condition :")) {
				hashMap.put("weatherCondition", (info.split(":"))[1].trim());
			} else if (info.contains("Wind:")) {
				String[] winds = info.split(":");
				winds = winds[1].split(" KPH Gusting to ");
				String windSpeedInKPH = winds[0].trim();
				winds = winds[1].split(" KPH");
				String windGust = winds[0];
				double windSpeedInKiloMeterPerHour = Double.parseDouble(windSpeedInKPH);
				double windSpeedInMeterPerSecond = windSpeedInKiloMeterPerHour / 3.6;
				double windSpeedInMilesPerHour = windSpeedInKiloMeterPerHour * 1.60934;
				hashMap.put("windSpeedInMeterPerSecond", Double.toString(windSpeedInMeterPerSecond));
				hashMap.put("windSpeedInMilesPerHour", Double.toString(windSpeedInMilesPerHour));
				double windGustInKiloMeterPerHour = Double.parseDouble(windGust);
				double windGustInMeterPerSecond = windGustInKiloMeterPerHour / 3.6;
				double windGustInMilesPerHour = windGustInKiloMeterPerHour * 1.60934;
				hashMap.put("windGustInMeterPerSecond", Double.toString(windGustInMeterPerSecond));
				hashMap.put("windGustInMilesPerHour", Double.toString(windGustInMilesPerHour));
			} else if (info.contains("Humidity:")) {
				String[] humidity = info.split(":");
				humidity = humidity[1].trim().split("%");
				hashMap.put("humidity", humidity[0]);
			} else if (info.contains("Temp in Degrees:")) {
				String tempInCelcius = info.split(":")[1].trim();
				double tempInDegreeCelcius = Double.parseDouble(tempInCelcius);
				double tempInKelvin = tempInDegreeCelcius + 273.15;
				hashMap.put("tempInCelcius", tempInCelcius);
				hashMap.put("tempInKelvin", Double.toString(tempInKelvin));
			} else if (info.contains("Temp in Fahrenheit:")) {
				hashMap.put("tempInFahrenheit", info.split(":")[1].trim());
			}
		}
		return hashMap;
	}

}
