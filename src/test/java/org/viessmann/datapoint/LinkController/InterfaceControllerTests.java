package org.viessmann.datapoint.LinkController;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
		InterfaceFactory controller = new InterfaceFactory();
		SerialInterface serialInterface = controller.createSerialInterface(testCommPort);
		
		assertNotNull(serialInterface);
		assertTrue(!serialInterface.getSerialPort().isClosed());
		serialInterface.close();
	}
	
	@Test
	public void createSerialInterfaceTwice() {
		InterfaceFactory controller = new InterfaceFactory();
		SerialInterface serialInterface1 = controller.createSerialInterface(testCommPort);
		SerialInterface serialInterface2 = controller.createSerialInterface(testCommPort);
		
		assertNotNull(serialInterface1);
		assertNull(serialInterface2);
		serialInterface1.close();
	}
	
	@Test
	public void closeSerialInterface() {
		InterfaceFactory controller = new InterfaceFactory();
		SerialInterface serialInterface = controller.createSerialInterface(testCommPort);
		
		serialInterface.close();
		assertTrue(serialInterface.isClosed());
		assertTrue(serialInterface.getSerialPort().isClosed());
	}

}
