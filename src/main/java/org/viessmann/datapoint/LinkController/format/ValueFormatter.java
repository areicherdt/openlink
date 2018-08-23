package org.viessmann.datapoint.LinkController.format;

import org.viessmann.datapoint.LinkController.configuration.Type;

public class ValueFormatter {

	public static String formatByteValues(byte[] buffer, Type type, int divider) {
	
		long result = 0;
		
		switch (type) {
		case BOOL:
			return buffer[0] == 0 ? "OFF" : "ON";
		case BYTE:
			break;
		case DATE:
			break;
		case INT:
			break;
		case SHORT:
			result = ((long)(buffer[1]))*0x100  + (long)(0xFF & buffer[0]);
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
		
		if(divider > 1) {
			return String.format("%.2f", (float)result / divider);
		} else {
			return String.format("%d", result);
		}
	}
}
