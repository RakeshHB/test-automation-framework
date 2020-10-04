package com.testvagrant.contract;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {

	@JsonProperty("speed")
	private Number speed;

	public Number getSpeed() {
		return speed;
	}

	public void setSpeed(Number speed) {
		this.speed = speed;
	}

}
