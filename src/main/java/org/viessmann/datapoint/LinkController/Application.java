package org.viessmann.datapoint.LinkController;

import org.viessmann.datapoint.LinkController.config.SparkConfig;
import org.viessmann.datapoint.LinkController.rest.InterfaceService;


public class Application {

	public static void main(String[] args) {

		SparkConfig.load();
		
		InterfaceService statusService = new InterfaceService();
		statusService.enableStatusService();
		
	}

}
