package org.viessmann.datapoint.LinkController.protocol;

import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.model.DataType;

public interface Protocol {

	public byte[] readData(SerialInterface serialInterface, int adress, DataType type);
}
