package org.openlink.datapoint.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.controller.CacheController;
import org.openlink.datapoint.controller.InterfaceController;
import org.openlink.datapoint.controller.ProtocolController;
import org.openlink.datapoint.model.DataType;
import org.openlink.datapoint.model.Datapoint;
import org.openlink.datapoint.protocol.SerialPortMockKW;
import org.openlink.datapoint.protocol.ViessmannKWProtocol;
import org.openlink.datapoint.protocol.command.DatapointOperationExecutor;
import org.openlink.datapoint.protocol.command.ReadDatapointOperation;

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
