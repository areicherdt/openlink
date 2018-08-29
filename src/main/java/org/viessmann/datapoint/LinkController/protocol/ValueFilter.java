package org.viessmann.datapoint.LinkController.protocol;

import org.viessmann.datapoint.LinkController.model.DataType;

import ch.qos.logback.classic.Logger;

public interface ValueFilter {

	public boolean doFilter(Object value, DataType type);
	public void setLogger(Logger logger);
}
