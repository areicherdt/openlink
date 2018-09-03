package org.viessmann.datapoint.LinkController.controller;


import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.format.ValueFormatter;
import org.viessmann.datapoint.LinkController.model.DataType;
import org.viessmann.datapoint.LinkController.protocol.Protocol;

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
