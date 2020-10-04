package com.seleniummaster.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class WeatherApiResponse {

	private final String USER_AGENT = "Mozilla/5.0";

	@SuppressWarnings("resource")
	public String getResponse() throws ClientProtocolException, IOException {
		StringBuffer result = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		String url = "http://api.openweathermap.org/data/2.5/weather?q=Bengaluru&appid=7fe67bf08c80ded756e598d6f8fedaea";
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);
		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("API Object:");
		System.out.println("===========");
		System.out.println("Response Code: " + responseCode);
		try {
			if (responseCode == 200)

			{
				System.out.println("Get Response is Successfull");
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			}
			return result.toString();

		} catch (Exception ex) {
			result.append("Get Response Failed");
			return result.toString();
		}

	}
}