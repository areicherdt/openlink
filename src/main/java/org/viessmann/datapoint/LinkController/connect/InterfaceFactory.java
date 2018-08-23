package org.viessmann.datapoint.LinkController.connect;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPort;
import org.openmuc.jrxtx.SerialPortBuilder;
import org.openmuc.jrxtx.StopBits;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

@Component
public class InterfaceFactory {
			
	Logger logger = (Logger) LoggerFactory.getLogger(InterfaceFactory.class);
	
	public SerialInterface createSerialInterface(String commPort) {

		if (listCommPorts().contains(commPort)) {
			try {
				SerialPort serialPort = SerialPortBuilder.newBuilder(commPort)
						.setBaudRate(4800)
						.setDataBits(DataBits.DATABITS_8)
						.setStopBits(StopBits.STOPBITS_2)
						.setParity(Parity.EVEN)
						.build();
				
				serialPort.setSerialPortTimeout(3000);
							
				logger.debug("Created serialPort for " + commPort);
				
				SerialInterface serialInterface = new SerialInterface(serialPort);
				return serialInterface;
				
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

		} else {
			logger.error(commPort + "not available. Cannot create connection.");
		}
		return null;
	}
	
	public static List<String> listCommPorts() {
		return Arrays.asList(SerialPortBuilder.getSerialPortNames());
	}

}
