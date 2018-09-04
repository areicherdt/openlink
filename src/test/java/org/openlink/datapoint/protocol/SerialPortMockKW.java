package org.openlink.datapoint.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.FlowControl;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPort;
import org.openmuc.jrxtx.StopBits;

public class SerialPortMockKW implements SerialPort {

	List<Integer> processList = new ArrayList<>();

	Queue<Integer> inputQueue = new LinkedList<>();
	Queue<Integer> outputQueue = new LinkedList<>();

	String portName;

	InputStream inputStream;
	OutputStream outputStream;

	public SerialPortMockKW(String portName) {
		this.portName = portName;

		this.inputStream = new InputStream() {
			@Override
			public int read() throws IOException {
				
				if(inputQueue.isEmpty()) {
					return 0;
				}
				
				return inputQueue.poll();
			}
		};

		this.outputStream = new OutputStream() {
			@Override
			public void write(int arg0) throws IOException {
				outputQueue.add(arg0);
				processAnswer();
			}
		};
		
		inputQueue.add(0x05);
	}

	protected void processAnswer() {

		Integer out = null;
		while ((out = outputQueue.poll()) != null) {
			Integer i = new Integer(out);
			processList.add(i);
		}

		if (processList.size() == 5 
				&& processList.get(0) == (byte) 0x01
				&& processList.get(1) == (byte) 0xF7
				&& processList.get(2) == (byte) 0x00
				&& processList.get(3) == (byte) 0xF8
				&& processList.get(4) == (byte) 0x02)  {
			inputQueue.add(0x20);
			inputQueue.add(0x98);
			processList = new ArrayList<>();
			
			inputQueue.add(0x05);
		}
		else if (processList.size() == 5 
				&& processList.get(0) == (byte) 0x01
				&& processList.get(1) == (byte) 0xF7
				&& processList.get(2) == (byte) 0x55
				&& processList.get(3) == (byte) 0x25
				&& processList.get(4) == (byte) 0x02)  {
			inputQueue.add(0x5B);
			inputQueue.add(0x00);
			processList = new ArrayList<>();
			
			inputQueue.add(0x05);
		}
		else if (processList.size() == 5 
				&& processList.get(0) == (byte) 0x01
				&& processList.get(1) == (byte) 0xF7
				&& processList.get(2) == (byte) 0x08
				&& processList.get(3) == (byte) 0x00
				&& processList.get(4) == (byte) 0x02)  {
			inputQueue.add(0x05);
			inputQueue.add(0x05);
			processList = new ArrayList<>();
			
			inputQueue.add(0x05);
		}
	}
		
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBaudRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DataBits getDataBits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowControl getFlowControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return outputStream;
	}

	@Override
	public Parity getParity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPortName() {
		return portName;
	}

	@Override
	public int getSerialPortTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public StopBits getStopBits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBaudRate(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataBits(DataBits arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFlowControl(FlowControl arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParity(Parity arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSerialPortTimeout(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStopBits(StopBits arg0) throws IOException {
		// TODO Auto-generated method stub

	}

}
