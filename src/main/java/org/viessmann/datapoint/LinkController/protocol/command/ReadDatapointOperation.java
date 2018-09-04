package org.viessmann.datapoint.LinkController.protocol.command;

import org.viessmann.datapoint.LinkController.controller.CacheController;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.model.Datapoint;

public class ReadDatapointOperation implements DatapointOperation {

	private ProtocolController protocolController;
	private CacheController cacheController;
	private Datapoint datapoint;
	
	public ReadDatapointOperation(ProtocolController protocolController, CacheController cacheController,
			Datapoint datapoint) {
		super();
		this.protocolController = protocolController;
		this.cacheController = cacheController;
		this.datapoint = datapoint;
	}

	@Override
	public String execute() {
		String result = protocolController.readAddress(datapoint.getAddress(), datapoint.getType());
		cacheController.put(datapoint.getChannel(), result);	
		return result;
	}
}
