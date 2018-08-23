package org.viessmann.datapoint.LinkController.protocol;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openmuc.jrxtx.SerialPort;
import org.viessmann.datapoint.LinkController.configuration.Type;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.format.ValueFormatter;

public class Viessmann300Test {
	
	Viessmann300Service protocolService;
	SerialInterface serialInterface;
	
	@Before
	public void init() {
		protocolService = new Viessmann300Service();		
		serialInterface = new SerialInterface(createSerialPortMock());		
	}
	
	@Test
	public void readSystemId() {
		
		int address = Integer.parseInt("00F8",16);
		
		byte[] result = protocolService.readData(serialInterface, address, Type.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x20, result[0]);
		assertEquals((byte)0xB8, result[1]);
	}
	
	@Test
	public void readOutsideTempWithFormatter() {
		int address = Integer.parseInt("5525",16);
		
		byte[] result = protocolService.readData(serialInterface, address, Type.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x07, result[0]);
		assertEquals((byte)0x01, result[1]);
		
		assertEquals("26,30", ValueFormatter.formatByteValues(result, Type.SHORT, 10));
	}
	
	private SerialPort createSerialPortMock() {
		try {			
			SerialPort serialPortMock = new SerialPortMock("COM3");		
			return serialPortMock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
