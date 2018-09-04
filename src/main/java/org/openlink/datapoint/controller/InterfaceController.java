package org.openlink.datapoint.controller;

import java.util.HashMap;
import java.util.Map;

import org.openlink.datapoint.connect.InterfaceFactory;
import org.openlink.datapoint.connect.InterfaceStatus;
import org.openlink.datapoint.connect.SerialInterface;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class InterfaceController {
	
	Logger logger = (Logger) LoggerFactory.getLogger(InterfaceController.class);
	
	private Map<String, SerialInterface> interfaces= new HashMap<>();
	
	public InterfaceController() {}
	
	public InterfaceController(String commPort) {
		getOrCreateInterface(commPort);
	}
	
	public SerialInterface getFirstInterface() {
		if(!interfaces.isEmpty() && interfaces.size() == 1) {
			return interfaces.values()
					.stream().findFirst().get();
		}
		return null;
	}
	
	public SerialInterface getOrCreateInterface(String commPort) {	
		if(!interfaces.containsKey(commPort)) {
			SerialInterface serialInterface = InterfaceFactory.createSerialInterface(commPort);
			interfaces.put(commPort, serialInterface);
			return serialInterface;
		} else {
			return interfaces.get(commPort);
		}
	}
	
	public void closeInterface(String commPort) {
		if(interfaces.containsKey(commPort)) {
			SerialInterface serialInterface = interfaces.get(commPort);
			serialInterface.close();
			interfaces.remove(commPort);
		} else {
			String msg = String.format("%s commPort not available.", commPort);
			logger.warn(msg);
			throw new RuntimeException(msg);
		}
	}
	
	public InterfaceStatus interfaceStatus(String commPort) {
		if(interfaces.containsKey(commPort)) {
			SerialInterface serialInterface = interfaces.get(commPort);
			if(serialInterface.isClosed()) {
				return InterfaceStatus.CLOSED;
			} else {
				return InterfaceStatus.OPEN;
			}
		} else {
			return InterfaceStatus.NOT_AVAILABLE;
		}
	}
	
	public void addInterface(String name, SerialInterface serialInterface) {
		interfaces.put(name, serialInterface);
	}
}
