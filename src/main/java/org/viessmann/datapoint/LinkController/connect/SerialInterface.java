package org.viessmann.datapoint.LinkController.connect;

import java.io.IOException;

import org.openmuc.jrxtx.SerialPort;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class SerialInterface {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SerialInterface.class);

	private SerialPort serialPort;
	
	public SerialInterface(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public synchronized int read() throws IOException {
		int data = -1;
		
		data = serialPort.getInputStream().read();
		if(data!=-1) {
			logger.debug("read {} <- {}", String.format("0x%02X", data), serialPort.getPortName());
		}
		else {
			logger.debug("read failed." + serialPort.getPortName());
		}

		return data;
	}
	
	public synchronized void write(int data) throws IOException {
		logger.debug("write {} -> {}", 
				String.format("0x%02X", data) ,serialPort.getPortName());		
		serialPort.getOutputStream().write(data);
	}
	
	public void close() {
		try {
			serialPort.close();
		} catch (IOException e) {
			logger.error("close error -> " + e.getMessage());
		}
	}
	
	public boolean isClosed() {
		return serialPort.isClosed();
	}
	
	public SerialPort getSerialPort() {
		return serialPort;
	}
	
	public synchronized void flush() {
		logger.debug("flush " + serialPort.getPortName());
		
		try {
			serialPort.getInputStream()
				.skip(serialPort.getInputStream().available());
		} catch (IOException e) {
			logger.error("flush error -> " + e.getMessage());
		}
	}

}
