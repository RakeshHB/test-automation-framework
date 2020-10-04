package com.testvagrant.scenarios;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.testvagrant.spec.User;

public class BaseScenario {
	WebDriver driver;
	User user;
	PageStore pageStore;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		pageStore = new PageStore();
		user = new User(pageStore);
		driver = pageStore.getDriver();
		driver.get(
				"https://www.ndtv.com/india-news/mumbai-rain-heavy-rain-in-city-and-neighbouring-areas-met-office-warns-of-more-rain-on-thursday-2278400");
	}

	@AfterMethod(alwaysRun = true)
	public void teardown(ITestResult result) {
		captureScreenShotInCaseOfFailure(result);
		pageStore.destroy();

	}

	protected <T extends User> T given(T dsl) {
		return dsl;
	}

	protected <T extends User> T when(T dsl) {
		return dsl;
	}

	protected <T extends User> T then(T dsl) {
		return dsl;
	}

	protected <T extends User> T and(T dsl) {
		return dsl;
	}

	protected void captureScreenShotInCaseOfFailure(ITestResult result) {
		if (result.isSuccess()) {
			return;
		}
		String screenShotFolder = "screenshots";
		File screenShotSourceFile = ((TakesScreenshot) pageStore.getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			createFolder(screenShotFolder);
			String fileName = result.getMethod().getMethodName();
			File screenShotTargetFile = getTargetFile(screenShotFolder, fileName, "png");
			FileUtils.copyFile(screenShotSourceFile, screenShotTargetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFolder(String folderName) throws IOException {
		if (!(new File(folderName).exists())) {
			new File(folderName).mkdir();
		}
	}

	private File getTargetFile(String folderName, String fileName, String ext) throws IOException {
		String rootPath = new File(".").getCanonicalPath();
		String fullPath = String.format("%s//%s//%s.%s", rootPath, folderName, fileName, ext);
		return new File(fullPath);
	}
}
