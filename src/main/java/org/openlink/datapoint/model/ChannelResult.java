package org.openlink.datapoint.model;

import java.util.Calendar;
import java.util.Date;

public class ChannelResult {
	
	private final String result;
	
	private final Date timestamp;

	public ChannelResult(String result) {
		super();
		this.result = result;
		this.timestamp = Calendar.getInstance().getTime();
	}

	public String getResult() {
		return result;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
