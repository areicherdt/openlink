package org.viessmann.datapoint.LinkController.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.controller.CacheController;
import org.viessmann.datapoint.LinkController.controller.InterfaceController;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.model.DataType;
import org.viessmann.datapoint.LinkController.model.Datapoint;
import org.viessmann.datapoint.LinkController.protocol.SerialPortMockKW;
import org.viessmann.datapoint.LinkController.protocol.ViessmannKWProtocol;
import org.viessmann.datapoint.LinkController.protocol.command.DatapointOperationExecutor;
import org.viessmann.datapoint.LinkController.protocol.command.ReadDatapointOperation;

public class DatapointExecutorTest {
	
	ProtocolController protocolController;
	ViessmannKWProtocol protocolService;
	SerialInterface serialInterface;
	InterfaceController interfaceController;
	
	@Before
	public void init() {
		protocolService = new ViessmannKWProtocol();
		serialInterface = new SerialInterface(new SerialPortMockKW("COM3"));
		interfaceController = new InterfaceController();
		interfaceController.addInterface("COM3", serialInterface);
		protocolController = new ProtocolController(protocolService, interfaceController);
	}
	
	

	@Test
	public void testWriteReadCache() throws InterruptedException {
		CacheController cache = new CacheController();
		Datapoint datapoint = new Datapoint();
		datapoint.setAddress("5525");
		datapoint.setCache(true);
		datapoint.setType(DataType.TEMP10);
		datapoint.setChannel("test");
		
		ReadDatapointOperation operation = new ReadDatapointOperation(protocolController, cache, datapoint);
		DatapointOperationExecutor executor = new DatapointOperationExecutor();
		executor.addTask(operation);
		
		Thread.sleep(1000);
		
		assertEquals("9.1", cache.read("test").getResult());
		Date time1 = cache.read("test").getTimestamp();
		
		executor.addTask(operation);
		
		Thread.sleep(1000);
		assertEquals("9.1", cache.read("test").getResult());
		Date time2 = cache.read("test").getTimestamp();
		
		System.out.println(time1);
		System.out.println(time2);
		
		assertNotEquals(time1, time2);
	}
	
}
