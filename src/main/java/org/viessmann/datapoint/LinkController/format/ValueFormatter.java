package org.viessmann.datapoint.LinkController.format;

import org.viessmann.datapoint.LinkController.model.DataType;

public class ValueFormatter {

	public static String formatByteValues(byte[] buffer, DataType type) {
	
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
		case BYTE:
			break;
		case DATE:
			break;
		case INT:
			break;
		case SHORT:
			result = getShort(buffer);
			break;
		case TIMER:
			break;
		case UBYTE:
			break;
		case UINIT:
			break;
		case USHORT:
			break;
		default:
			break;
		}
		
		if(type.getDivider() > 1) {
			return String.format("%.2f", (float)result / type.getDivider());
		} else {
			return String.format("%d", result);
		}
	}
	
	public static long getShort(byte[] buffer) {
		return ((long)(buffer[1]))*0x100  + (long)(0xFF & buffer[0]);
	}
}
