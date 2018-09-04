package org.openlink.datapoint.protocol;

import org.openlink.datapoint.model.DataType;

import ch.qos.logback.classic.Logger;

public interface ValueFilter {

	public boolean doFilter(Object value, DataType type);
	public void setLogger(Logger logger);
}
