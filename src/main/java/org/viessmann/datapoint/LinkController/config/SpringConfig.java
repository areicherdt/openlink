package org.viessmann.datapoint.LinkController.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.viessmann.datapoint.LinkController.connect.InterfaceController;
import org.viessmann.datapoint.LinkController.rest.InterfaceService;
import org.viessmann.datapoint.LinkController.util.YamlLoader;
import org.viessmann.datapoint.LinkController.rest.DatapointService;

@Configuration
public class SpringConfig {

	@Bean
	public InterfaceController interfaceController() {
		YamlLoader loader = yamlLoader();
		ApplicationConfig cfg = loader.loadYamlConfiguration();
		
		if(cfg.getPort()!=null && !cfg.getPort().isEmpty()) {
			return new InterfaceController(cfg.getPort());
		}
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
	
	@Bean
	public YamlLoader yamlLoader() {
		return new YamlLoader();
	}
}
