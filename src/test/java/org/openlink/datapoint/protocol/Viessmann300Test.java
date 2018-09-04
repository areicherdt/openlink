package org.openlink.datapoint.protocol;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.format.ValueFormatter;
import org.openlink.datapoint.model.DataType;
import org.openlink.datapoint.protocol.Viessmann300Protocol;

public class Viessmann300Test {
	
	Viessmann300Protocol protocolService;
	SerialInterface serialInterface;
	
	@Before
	public void init() {
		protocolService = new Viessmann300Protocol();		
		serialInterface = new SerialInterface(new SerialPortMock300("COM3"));		
	}
	
	@Test
	public void readSystemId() {
		
		int address = Integer.parseInt("00F8",16);
		
		byte[] result = protocolService.readData(serialInterface, address, DataType.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x20, result[0]);
		assertEquals((byte)0xB8, result[1]);
	}
	
	@Test
	public void readOutsideTempWithFormatter() {
		int address = Integer.parseInt("5525",16);
		
		byte[] result = protocolService.readData(serialInterface, address, DataType.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x07, result[0]);
		assertEquals((byte)0x01, result[1]);
		
		assertEquals("263", ValueFormatter.formatByteValuesToString(result, DataType.SHORT));
	}
}
