package org.openlink.datapoint.protocol;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.format.ValueFormatter;
import org.openlink.datapoint.model.DataType;
import org.openlink.datapoint.protocol.TemperatureFilter;
import org.openlink.datapoint.protocol.ValueFilter;
import org.openlink.datapoint.protocol.ViessmannKWProtocol;

public class ViessmannKWTest {

	ViessmannKWProtocol protocolService;
	SerialInterface serialInterface;
	
	@Before
	public void init() {
		List<ValueFilter> filterList = new ArrayList<>();
		filterList.add(new TemperatureFilter());
		protocolService = new ViessmannKWProtocol(filterList);
		serialInterface = new SerialInterface(new SerialPortMockKW("COM3"));		
	}
	
	@Test
	public void readSystemId() {
		
		int address = Integer.parseInt("00F8",16);
		
		byte[] result = protocolService.readData(serialInterface, address, DataType.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x20, result[0]);
		assertEquals((byte)0x98, result[1]);
	}
	
	@Test
	public void readOutsideTempWithFormatter() {
		
		int address = Integer.parseInt("5525",16);
		
		byte[] result = protocolService.readData(serialInterface, address, DataType.SHORT);
		serialInterface.close();
		
		assertEquals((byte)0x5B, result[0]);
		assertEquals((byte)0x00, result[1]);
		assertEquals("91", ValueFormatter.formatByteValuesToString(result, DataType.SHORT));
	}
	
	@Test
	public void readTempWithFilterAndRetry() {
		
		int address = Integer.parseInt("0800",16);
		byte[] result = protocolService.readData(serialInterface, address, DataType.TEMP10);
		assertEquals(null, result);
	}
	
	//@Test(expected = RuntimeException.class)
	public void readDataAckTimeout() throws IOException {
		
		serialInterface.read();
		int address = Integer.parseInt("00F8",16);
		
		protocolService.readData(serialInterface, address, DataType.SHORT);
		serialInterface.close();
	}
	
}
