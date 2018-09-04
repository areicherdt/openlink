package org.openlink.datapoint.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.format.ValueFormatter;
import org.openlink.datapoint.model.DataType;
import org.openmuc.jrxtx.SerialPortTimeoutException;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class ViessmannKWProtocol implements Protocol {
	
	Logger logger = (Logger) LoggerFactory.getLogger(ViessmannKWProtocol.class);

	public static final int ACK = 0x05;
	public static final int ACK_REPLY = 0x01;
	public static final int READ = 0xF7;	
		
	private static final long TIMEOUT = 1000;
	
	private List<ValueFilter> filters = new ArrayList<>();
	
	public ViessmannKWProtocol() {}
	
	public ViessmannKWProtocol(List<ValueFilter> filters) {
		this.filters = filters;
	}
	
	@Override
	public synchronized byte[] readData(SerialInterface serialInterface, int adress, DataType type) {
		
		byte [] result = null;
		int retry = 0;
		boolean ok = true;
		
		while(retry < 3) {
			result = readAndFilterdata(serialInterface, adress, type);
			Object o = ValueFormatter.formatByteValues(result, type);
			for(ValueFilter filter : filters) {
				filter.setLogger(logger);
				if(!filter.doFilter(o, type)) {
					retry++;
					ok = false;
					break;
				}
			}
			if(ok) {
				break;
			} else {
				continue;
			}
		}
		return result;
	}
	

	private synchronized byte[] readAndFilterdata(SerialInterface serialInterface, int adress, DataType type) {
		
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
