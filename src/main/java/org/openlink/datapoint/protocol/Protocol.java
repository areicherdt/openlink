package org.openlink.datapoint.protocol;

import org.openlink.datapoint.connect.SerialInterface;
import org.openlink.datapoint.model.DataType;

public interface Protocol {

	public byte[] readData(SerialInterface serialInterface, int adress, DataType type);
}
