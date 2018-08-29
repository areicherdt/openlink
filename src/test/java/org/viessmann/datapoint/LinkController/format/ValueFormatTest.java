package org.viessmann.datapoint.LinkController.format;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.viessmann.datapoint.LinkController.model.DataType;

public class ValueFormatTest {

	@Test
	public void testTemp10() {
		byte[] buffer = {0x5B, 0x00};
		String resultStr = ValueFormatter.formatByteValuesToString(buffer, DataType.TEMP10);
		float result = (Float) ValueFormatter.formatByteValues(buffer, DataType.TEMP10);
		
		assertEquals(9.1f, result, 0.0);
		assertEquals("9,10", resultStr);
	}
	
	@Test
	public void testTemp100() {
		byte[] buffer = {0x5B, 0x00};
		String resultStr = ValueFormatter.formatByteValuesToString(buffer, DataType.TEMP100);
		float result = (Float) ValueFormatter.formatByteValues(buffer, DataType.TEMP100);
		
		assertEquals(0.91f, result, 0.0);
		assertEquals("0,91", resultStr);
	}
	
	@Test
	public void testBool() {
		byte[] buffer1 = {0x00};
		String resultOFF = (String) ValueFormatter.formatByteValues(buffer1, DataType.BOOL);
		byte[] buffer2 = {0x01};
		String resultON = ValueFormatter.formatByteValuesToString(buffer2, DataType.BOOL);
		
		assertEquals("OFF", resultOFF);
		assertEquals("ON", resultON);
	}
	
	@Test
	public void testDate() {
		byte[] buffer = {0x20, 0x18, 0x01, 0x01, 0x05, 0x06, 0x07, 0x08};
		String resultStr = ValueFormatter.formatByteValuesToString(buffer, DataType.DATE);
		
		assertEquals("2018-01-01T06:07:08", resultStr);			
	}
	
	@Test
	public void testByte2() {
		byte[] buffer = {0x20, (byte) 0xC8};
		String resultStr = ValueFormatter.formatByteValuesToString(buffer, DataType.BYTE2);
		
		assertEquals("0x20C8", resultStr);			
	}
	
	@Test
	public void testByte1() {
		byte[] buffer = {0x1C};
		long result = (long) ValueFormatter.formatByteValues(buffer, DataType.BYTE);
		String resultStr = ValueFormatter.formatByteValuesToString(buffer, DataType.BYTE);
		assertEquals(28l, result);		
		assertEquals("28", resultStr);	
	}
	
	@Test
	public void testOil() {
		byte[] buffer = {0x00, 0x00, 0x01, 0x00};
		float result = (float) ValueFormatter.formatByteValues(buffer, DataType.OIL);
		assertEquals(65.536f, result, 0);		
	}
	
}
