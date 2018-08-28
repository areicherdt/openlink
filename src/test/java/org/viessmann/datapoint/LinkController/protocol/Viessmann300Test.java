package org.viessmann.datapoint.LinkController.protocol;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.format.ValueFormatter;
import org.viessmann.datapoint.LinkController.model.DataType;

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
		
		assertEquals("263", ValueFormatter.formatByteValues(result, DataType.SHORT));
	}
}
