package org.viessmann.datapoint.LinkController;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.viessmann.datapoint.LinkController.connect.InterfaceFactory;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;

public class InterfaceControllerTests {
	
	private String testCommPort = "COM3";

	@Test
	public void listCommPorts() {
		assertTrue(InterfaceFactory.listCommPorts().size() >= 1);
		InterfaceFactory.listCommPorts().forEach(port -> assertTrue(port.getClass().equals(String.class)));		
	}
	
	@Test
	public void createSerialInterface() {
		SerialInterface serialInterface = InterfaceFactory.createSerialInterface(testCommPort);
		
		assertNotNull(serialInterface);
		assertTrue(!serialInterface.getSerialPort().isClosed());
		serialInterface.close();
	}
	
	@Test
	public void createSerialInterfaceTwice() {
		SerialInterface serialInterface1 = InterfaceFactory.createSerialInterface(testCommPort);
		assertNotNull(serialInterface1);

		try {
			InterfaceFactory.createSerialInterface(testCommPort);	
			fail("RuntimeException expected!");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		} finally {
			serialInterface1.close();
		}
	}
	
	@Test
	public void closeSerialInterface() {
		SerialInterface serialInterface = InterfaceFactory.createSerialInterface(testCommPort);
		
		serialInterface.close();
		assertTrue(serialInterface.isClosed());
		assertTrue(serialInterface.getSerialPort().isClosed());
	}

}
