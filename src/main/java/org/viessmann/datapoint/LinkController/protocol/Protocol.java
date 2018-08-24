package org.viessmann.datapoint.LinkController.protocol;

import org.viessmann.datapoint.LinkController.configuration.Type;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;

public interface Protocol {

	public byte[] readData(SerialInterface serialInterface, int adress, Type type);
}
