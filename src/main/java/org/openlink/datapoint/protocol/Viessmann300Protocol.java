package org.openlink.datapoint.protocol;

import java.io.IOException;

import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.model.DataType;
import org.openmuc.jrxtx.SerialPortTimeoutException;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class Viessmann300Protocol implements Protocol {
	
	Logger logger = (Logger) LoggerFactory.getLogger(Viessmann300Protocol.class);
	
	public static final int NULL 	= 0x00;
	public static final int CLOSE	= 0x04;
	public static final int ACK 	= 0x06;
	public static final int INIT 	= 0x16;
	public static final int START 	= 0x41;

	
	public synchronized byte[] readData(SerialInterface serialInterface, int adress, DataType type) {
		
		byte[] transmitBuffer = {0x00, 0x01, (byte)(adress >> 8), (byte)(adress & 0xff), (byte)type.getLength()};
		byte[] resultBuffer = new byte[16];
		
		if(serialInterface != null && startSession(serialInterface)) {
			logger.debug("session start successful");
			
			for(int i=0;i<3;i++) {
				if(transferData(serialInterface, transmitBuffer, 5)) {
					int resultBytes = receiveData(serialInterface, resultBuffer);
					
					if(resultBytes > 0) {
						return handleReadResult(resultBuffer, adress, type.getLength()); //OK
					}
					else {
						logger.warn("communication failed. retry #{}", i);
			            startSession(serialInterface);
			            serialInterface.flush();
					}
				}
			}
		} else {
			logger.error("cannot start session for " + serialInterface.getSerialPort().getPortName());
		}
		throw new RuntimeException("communication problem with " + serialInterface.getSerialPort().getPortName());
	}
	
	private byte[] handleReadResult(byte[] resultBuffer, int adress, int length) {
		byte [] buffer = new byte[length];
		
		if (resultBuffer[0] == 0x03) {
			logger.error("answer byte is 0x03: return error(Wrong Adress?)");
		}
		else if (resultBuffer[0] != 0x01) {
			logger.error("answer byte (0x01) expected but was 0x{} ", resultBuffer[0]);
		}
		
		if (resultBuffer[1] != 0x01) {
			logger.error("deadRead byte (0x01) expected but was 0x{} ", resultBuffer[1]);
		}
		
		int returnAddress = ((resultBuffer[2] & 0xFF) << 8) + ((int)resultBuffer[3] & 0xFF);
		if(returnAddress != adress) {
			logger.error("adress mismtach -> expected {} but was {}", adress, returnAddress);
		}
		
		for (int i=0;i<resultBuffer[4];i++) {
			buffer[i] = resultBuffer[i+5]; 
		}
		return buffer;
	}
	
	private boolean startSession(SerialInterface serialInterface) {
		try {
			serialInterface.flush();
			serialInterface.write(CLOSE);
			serialInterface.read();
			
			for (int i=0; i<5; i++) { 
				serialInterface.write(INIT);
				serialInterface.write(NULL);
				serialInterface.write(NULL);
				
				if(serialInterface.read() == ACK) {				
					return true;
				}
			}
			
		} catch (SerialPortTimeoutException e) {
			logger.error("startSession timeout -> " + serialInterface.getSerialPort().getPortName());
		} catch (Exception e) {
			logger.error("startSession error -> " + e.getMessage());
		}
		return false;
	}
	
	private boolean transferData(SerialInterface serialInterface, byte[] buffer, int len) {
		try {
			serialInterface.write(START);
			serialInterface.write(len);
			for(int i=0;i<len;i++) {
				serialInterface.write(buffer[i]);
			}
			serialInterface.write(checksum(buffer, len) & 0xFF);
			
			int result = serialInterface.read();
			if(result != ACK) {
				logger.error("ack expected ({}) but was {}", String.format("0x%02X", ACK),result);
				return false;
			} else {
				logger.debug("transfer data to interface successful");
			}
		} catch (SerialPortTimeoutException e) {
			logger.error("transferData timeout -> " + serialInterface.getSerialPort().getPortName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private int receiveData(SerialInterface serialInterface, byte[] resultBuffer) {
		
		try {
			int result = serialInterface.read();
			if(result != START) {
				logger.error("START -> expected {} but was {}", START, result);
				serialInterface.flush();
				return -1;
			}
						
			int resultLength = serialInterface.read();
			int resultChecksum = resultChecksum(serialInterface, resultBuffer, resultLength);
			int bufferChecksum = serialInterface.read();
			
			if(bufferChecksum != resultChecksum) {
				logger.error("resultChecksum mismatch -> expected {} but was {}", bufferChecksum, resultChecksum);
			}
			return resultLength;
		} catch (SerialPortTimeoutException e) {
			logger.error("receiveData timeout -> " + serialInterface.getSerialPort().getPortName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return 0;
	}
	
	private int resultChecksum(SerialInterface serialInterface, byte[] buffer, int resultLength) throws IOException {
		int resultChecksum = resultLength;
		for (int i=0;i<resultLength;i++){
            buffer[i]=(byte)serialInterface.read();
            resultChecksum+=buffer[i];
		}
		return resultChecksum & 0xFF; 
	}
	
	private int checksum(byte[] buffer, int len) {
		int checksum = len;
		for(int i=0;i<len;i++) {
			checksum+=buffer[i];
		}
		return checksum;
	}
}
