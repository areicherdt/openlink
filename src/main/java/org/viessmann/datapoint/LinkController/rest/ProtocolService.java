package org.viessmann.datapoint.LinkController.rest;

import org.viessmann.datapoint.LinkController.connect.InterfaceController;

public class ProtocolService {

	private static final String RETURN_TYPE = "application/json";

	private InterfaceController interfaceController;
	
	public ProtocolService(InterfaceController interfaceController) {
		this.interfaceController = interfaceController;
		init();
	}
	
	private void init() {
		
	}
}
