package org.openlink.datapoint.protocol.command;

import org.openlink.datapoint.controller.CacheController;
import org.openlink.datapoint.controller.ProtocolController;
import org.openlink.datapoint.model.Datapoint;

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
