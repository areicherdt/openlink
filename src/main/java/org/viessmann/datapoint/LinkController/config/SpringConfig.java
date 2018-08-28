package org.viessmann.datapoint.LinkController.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.viessmann.datapoint.LinkController.rest.InterfaceService;
import org.viessmann.datapoint.LinkController.util.YamlLoader;
import org.viessmann.datapoint.LinkController.controller.InterfaceController;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.protocol.Viessmann300Protocol;
import org.viessmann.datapoint.LinkController.protocol.ViessmannKWProtocol;
import org.viessmann.datapoint.LinkController.rest.DatapointService;

@Configuration
public class SpringConfig {

	@Bean
	public ApplicationConfig applicationConfig() {
		YamlLoader loader = yamlLoader();
		return loader.loadConfiguration();
	}

	@Bean
	public InterfaceController interfaceController() {
		String port = applicationConfig().getPort();
		if (port != null && !port.isEmpty()) {
			return new InterfaceController(port);
		}
		throw new RuntimeException("no valid default port configured.");
	}

	@Bean
	public ProtocolController protocolController() {
		switch (applicationConfig().getProtocol()) {
		case KW:
			return new ProtocolController(new ViessmannKWProtocol(), interfaceController());
		case V300:
			return new ProtocolController(new Viessmann300Protocol(), interfaceController());
		default:
			throw new RuntimeException("no valid protocol configured.");
		}
	}

	@Bean
	public DatapointService datapointService() {
		return new DatapointService(protocolController(), yamlLoader().loadDatapoints());
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
