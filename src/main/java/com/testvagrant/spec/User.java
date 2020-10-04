package com.testvagrant.spec;

import org.testng.Assert;

import com.testvagrant.domain.CityDetails;
import com.testvagrant.pages.LandingPage;
import com.testvagrant.pages.SearchResultsPage;
import com.testvagrant.pages.WeatherSearchPage;
import com.testvagrant.scenarios.PageStore;

public class User {

	PageStore pageStore;

	public User(PageStore pageStore) {
		this.pageStore = pageStore;
	}

	public void searchesForACityWith(CityDetails cityDetails) {
		Assert.assertTrue(pageStore.get(WeatherSearchPage.class).searchesForACityWeatherWith(cityDetails));
	}

	public void hasWeatherReportsAvailableForHisCity(CityDetails cityDetails) {
		Assert.assertTrue(pageStore.get(SearchResultsPage.class).resultsAppearForSelectedCity(cityDetails));
	}

	public void choosesToDoAWeatherSearch() {
		LandingPage onLandingPage = pageStore.get(LandingPage.class);
		onLandingPage.hoverOnMenu();
		onLandingPage.goToWeatherReportPage();
	}
}
