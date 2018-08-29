package org.viessmann.datapoint.LinkController.format;

import org.viessmann.datapoint.LinkController.model.DataType;

public class ValueFormatter {

	public static String formatByteValuesToString(byte[] buffer, DataType type) {

		Object result = formatByteValues(buffer, type);
		if(result instanceof String) {
			return (String) result;
		}
		
		if(type.getDivider() > 1) {
			return String.format("%.2f", (float)result);
		} else {
			return String.format("%d", result);
		}
	}
	
	public static Object formatByteValues(byte[] buffer, DataType type) {
		
		long result = 0;
		
		switch (type) {
		case TEMP10:
			result = getShort(buffer);
			break;
		case TEMP100:
			result = getShort(buffer);
			break;
		case BOOL:
			return buffer[0] == 0 ? "OFF" : "ON";
		case DATE:
			return getDate(buffer);
		case SHORT:
			result = getShort(buffer);
			break;
		case BYTE2:
			return getByte2String(buffer);
		case BYTE:
			result = (long)(0xFF & buffer[0]);
			break;
		case OIL:
			result = getByte4(buffer);
			break;
		default: throw new RuntimeException("unhandled dataype" + type);
		}
		
		if(type.getDivider() > 1) {
			return (float)result / type.getDivider();
		} else {
			return result;
		}
	}
	
	private static long getByte4(byte[] buffer) {
		return ((long)(buffer[3]))*0x1000000  + ((long)(0xFF & buffer[2]))*0x10000  
				+ ((long)(0xFF & buffer[1]))*0x100  + (long)(0xFF & buffer[0]);
	}

	private static Object getByte2String(byte[] buffer) {
		return String.format("0x%02X%02X", buffer[0], buffer[1]);
	}

	public static long getShort(byte[] buffer) {
		return ((long)(buffer[1]))*0x100  + (long)(0xFF & buffer[0]);
	}
	
	public static String getDate(byte[] buffer) {
		return String.format("%02x%02x-%02x-%02xT%02x:%02x:%02x",
				buffer[0],buffer[1],buffer[2],buffer[3],buffer[5],buffer[6],buffer[7]);
	}
}
