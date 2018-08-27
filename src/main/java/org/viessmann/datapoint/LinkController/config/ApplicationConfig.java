package org.viessmann.datapoint.LinkController.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.viessmann.datapoint.LinkController.connect.InterfaceController;
import org.viessmann.datapoint.LinkController.rest.InterfaceService;
import org.viessmann.datapoint.LinkController.rest.DatapointService;

@Configuration
public class ApplicationConfig {

	@Bean
	public InterfaceController interfaceController() {
		return new InterfaceController();
	}
	
	@Bean
	public DatapointService ProtocolService() {
		return new DatapointService(interfaceController());
	}
	
	@Bean
	public InterfaceService interfaceService() {
		return new InterfaceService(interfaceController());
	}
}
