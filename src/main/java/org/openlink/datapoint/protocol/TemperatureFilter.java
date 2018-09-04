package org.openlink.datapoint.protocol;

import org.openlink.datapoint.model.DataType;

import ch.qos.logback.classic.Logger;

public class TemperatureFilter implements ValueFilter {

	private Logger logger;
	
	private static final int MAX = 85;
	
	@Override
	public boolean doFilter(Object o, DataType type) {
		
		if(o instanceof Number 
				&& (type.equals(DataType.TEMP10) || type.equals(DataType.TEMP100)) ) {
			Number num = (Number) o;
			
			if(num.floatValue() > MAX) {
				if(logger!=null) {
					logger.debug("{} value {} > {}", TemperatureFilter.class, num, MAX);
				}
				return false;
			} else {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
