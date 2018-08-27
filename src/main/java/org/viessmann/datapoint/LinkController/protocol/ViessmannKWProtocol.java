package org.viessmann.datapoint.LinkController.protocol;

import java.io.IOException;

import org.openmuc.jrxtx.SerialPortTimeoutException;
import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.configuration.Type;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;

import ch.qos.logback.classic.Logger;

public class ViessmannKWProtocol implements Protocol {
	
	Logger logger = (Logger) LoggerFactory.getLogger(ViessmannKWProtocol.class);

	public static final int ACK = 0x05;
	public static final int ACK_REPLY = 0x01;
	public static final int READ = 0xF7;	
		
	private static final long TIMEOUT = 1000;
	
	@Override
	public synchronized byte[] readData(SerialInterface serialInterface, int adress, Type type) {
		
		byte[] buffer = new byte [type.getLen()];
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
			serialInterface.write((byte) type.getLen());
			
			for(int i=0; i < type.getLen(); i++) {
				buffer[i] = (byte) serialInterface.read();
			}
			return buffer;
			
		} catch (SerialPortTimeoutException e) {
			logger.error("readData timeout -> {}", serialInterface.getSerialPort().getPortName());
		} catch (IOException e) {
			logger.error("readData error -> " + e.getMessage());
		}

		return null;
	}

}
