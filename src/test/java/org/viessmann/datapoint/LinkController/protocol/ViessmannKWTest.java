package org.viessmann.datapoint.LinkController.protocol;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.format.ValueFormatter;

public class ViessmannKWTest {

	ViessmannKWProtocol protocolService;
	SerialInterface serialInterface;
	
	@Before
	public void init() {
		protocolService = new ViessmannKWProtocol();
		serialInterface = new SerialInterface(new SerialPortMockKW("COM3"));		
	}
	
	@Test
	public void readSystemId() {
		
		int address = Integer.parseInt("00F8",16);
		
		byte[] result = protocolService.readData(serialInterface, address, Type.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x20, result[0]);
		assertEquals((byte)0x98, result[1]);
	}
	
	@Test
	public void readOutsideTempWithFormatter() {
		
		int address = Integer.parseInt("5525",16);
		
		byte[] result = protocolService.readData(serialInterface, address, Type.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x5B, result[0]);
		assertEquals((byte)0x00, result[1]);
		assertEquals("9,10", ValueFormatter.formatByteValues(result, Type.SHORT, 10));
	}
	
	//@Test(expected = RuntimeException.class)
	public void readDataAckTimeout() throws IOException {
		
		serialInterface.read();
		int address = Integer.parseInt("00F8",16);
		
		protocolService.readData(serialInterface, address, Type.SHORT);
		serialInterface.close();
	}
	
}
