package org.openlink.datapoint.connect;

import java.io.IOException;

import org.openmuc.jrxtx.SerialPort;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class SerialInterface {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SerialInterface.class);

	private final SerialPort serialPort;
	
	public SerialInterface(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public synchronized int read() throws IOException {
		int data = serialPort.getInputStream().read();
		if(data!=-1) {
			logger.trace("read {} <- {}", String.format("0x%02X", data), serialPort.getPortName());
		}
		else {
			logger.debug("read failed." + serialPort.getPortName());
		}
		return data;
	}
	
	public synchronized void write(int data) throws IOException {
		logger.trace("write {} -> {}", 
				String.format("0x%02X", (byte) data) ,serialPort.getPortName());		
		serialPort.getOutputStream().write((byte)data);
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
		logger.trace("flush " + serialPort.getPortName());
		
		try {
			serialPort.getInputStream()
				.skip(serialPort.getInputStream().available());
		} catch (IOException e) {
			logger.error("flush error -> " + e.getMessage());
		}
	}

}
