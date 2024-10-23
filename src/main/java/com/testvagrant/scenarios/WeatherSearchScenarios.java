package com.testvagrant.scenarios;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.testvagrant.contract.ResponseContainer;
import com.testvagrant.domain.CityDetails;
import com.testvagrant.domain.CityDetailsBuilder;

public class WeatherSearchScenarios extends BaseScenario {

	static ResponseContainer responseContainer;

	@Test
	public void testThatResultsAppearForACity() throws IOException {
		String city = "Ahmedabad";
		CityDetails cityDetails = new CityDetailsBuilder().withCityName(city).build();
		given(user).choosesToDoAWeatherSearch();
		when(user).searchesForACityWith(cityDetails);
		then(user).hasWeatherReportsAvailableForHisCity(cityDetails);
		String responseString = RestAssured.given().param("q", city)
				.param("appid", "7fe67bf08c80ded756e598d6f8fedaea").when()
				.get("http://api.openweathermap.org/data/2.5/weather").asString();
		ObjectMapper mapper = new ObjectMapper();
		responseContainer = mapper.readValue(responseString, ResponseContainer.class);
		boolean isTempTestPassed = customAssert(40, CityDetails.getTempInKelvin(), responseContainer.getMain().getTemp(), "TempInKelvin");
		boolean isHumidityTestPassed = customAssert(40, CityDetails.getHumidity(), responseContainer.getMain().getHumidity(), "Humidity");
		boolean isWindSpeedTestPassed = customAssert(40, CityDetails.getWindSpeedInMeterPerSecond(),
				responseContainer.getWind().getSpeed(), "WindSpeedInMeterPerSecond");
		if(isTempTestPassed && isHumidityTestPassed && isWindSpeedTestPassed) {
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}
	}


	private boolean customAssert(double allowedDifference, Object o1, Object o2, String testCase) {
		double diff = compare(o1, o2, allowedDifference);
		boolean isTestPassed = true;
		if (diff != 0.0) {
			isTestPassed = false;
			System.out.println("TestCase Name: " + testCase);
			System.out.println("Marking this test as failed");
			System.out.println("Value got/derived from UI: " + o1);
			System.out.println("Value got from API: " + o2);
			System.out.println("Difference between values: " + diff + " which is greater than allowed difference by "
					+ (diff - allowedDifference));
			System.out.println("Allowed difference " + allowedDifference);
		}
		return isTestPassed;
	}

	private double compare(Object o1, Object o2, double allowedDiff) {
		double diff = 0.0;
		double value1 = Double.parseDouble(o1.toString());
		double value2 = Double.parseDouble(o2.toString());
		diff = Math.abs(value1 - value2);
		if (diff <= allowedDiff) {
			return 0;
		}
		return diff;
	}
}
