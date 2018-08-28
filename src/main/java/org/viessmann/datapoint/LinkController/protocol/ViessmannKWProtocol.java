package org.viessmann.datapoint.LinkController.protocol;

import java.io.IOException;

import org.openmuc.jrxtx.SerialPortTimeoutException;
import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.model.DataType;

import ch.qos.logback.classic.Logger;

public class ViessmannKWProtocol implements Protocol {
	
	Logger logger = (Logger) LoggerFactory.getLogger(ViessmannKWProtocol.class);

	public static final int ACK = 0x05;
	public static final int ACK_REPLY = 0x01;
	public static final int READ = 0xF7;	
		
	private static final long TIMEOUT = 1000;
	
	@Override
	public synchronized byte[] readData(SerialInterface serialInterface, int adress, DataType type) {
		
		byte[] buffer = new byte [type.getLength()];
		serialInterface.flush();
		
		long wait= System.currentTimeMillis() + TIMEOUT;
		
		try {
			while(serialInterface.read() != ACK) {
				long now = System.currentTimeMillis();
				if(now > wait) {
					throw new RuntimeException(String.format("no ACK (%s) received within %s ms", ACK, TIMEOUT));
				}
			}
			
			serialInterface.write(ACK_REPLY);
			serialInterface.write(READ);
			serialInterface.write((byte) (adress >> 8));
			serialInterface.write((byte) (adress & 0xff));
			serialInterface.write((byte) type.getLength());
			
			for(int i=0; i < type.getLength(); i++) {
				buffer[i] = (byte) serialInterface.read();
			}
			return buffer;
			
		} catch (SerialPortTimeoutException e) {
			String msg = String.format("readData timeout -> %s", serialInterface.getSerialPort().getPortName());
			logger.error(msg);
			throw new RuntimeException(msg);
		} catch (IOException e) {
			logger.error("readData error -> " + e.getMessage());
		}

		return null;
	}

}
