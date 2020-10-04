
package com.testvagrant.contract;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Main{

	@JsonProperty("temp")
	private Number temp;

	@JsonProperty("humidity")
	private Number humidity;

	public Number getTemp() {
		return temp;
	}

	public void setTemp(Number temp) {
		this.temp = temp;
	}

	public Number getHumidity() {
		return humidity;
	}

	public void setHumidity(Number humidity) {
		this.humidity = humidity;
	}

}
