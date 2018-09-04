package org.openlink.datapoint.controller;


import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.format.ValueFormatter;
import org.openlink.datapoint.model.DataType;
import org.openlink.datapoint.protocol.Protocol;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class ProtocolController {
		
	Logger logger = (Logger) LoggerFactory.getLogger(ProtocolController.class);

	private Protocol protocolHandler;
	private InterfaceController interfaceController;	
	
	public ProtocolController(Protocol protocolHandler, InterfaceController interfaceController) {
		this.protocolHandler = protocolHandler;
		this.interfaceController = interfaceController;
	}

	public String readAddress(String address, DataType dataType) {
		logger.debug("read data for address {} with type {}", address, dataType);
		
		SerialInterface serialInterface = interfaceController.getFirstInterface();
		int byteAddress = Integer.parseInt(address, 16);
		byte[] result = protocolHandler.readData(serialInterface, byteAddress , dataType);
		
		return formatValue(result, dataType);
	}
	
	public String readAddressWithStringType(String address, String type) {
		return readAddress(address, DataType.valueOf(type));		
	}
	
	private String formatValue(byte[] array, DataType dataType) {
		
		if(array!=null) {
			return ValueFormatter.formatByteValuesToString(array, dataType);
		}

		return null;
	}
	
	
}
