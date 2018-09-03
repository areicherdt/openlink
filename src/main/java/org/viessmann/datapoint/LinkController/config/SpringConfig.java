package org.viessmann.datapoint.LinkController.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.viessmann.datapoint.LinkController.rest.InterfaceService;
import org.viessmann.datapoint.LinkController.scheduler.SchedulerService;
import org.viessmann.datapoint.LinkController.util.YamlLoader;
import org.viessmann.datapoint.LinkController.config.model.Database;
import org.viessmann.datapoint.LinkController.config.model.Schedule;
import org.viessmann.datapoint.LinkController.controller.InterfaceController;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.db.InfluxService;
import org.viessmann.datapoint.LinkController.protocol.TemperatureFilter;
import org.viessmann.datapoint.LinkController.protocol.ValueFilter;
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
			return new ProtocolController(new ViessmannKWProtocol(filterChain()), interfaceController());
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
	
	@Bean
	public InfluxService influxService() {
		Database dbconfig = applicationConfig().getDatabase();
		if(dbconfig.getUrl() != null && !dbconfig.getUrl().isEmpty()) {
			return new InfluxService(dbconfig);
		}
		return null;
	}
	
	@Bean
	public SchedulerService schedulerService() {
		Schedule scheduleConfig = applicationConfig().getSchedule();
		if(scheduleConfig.isEnabled()) {
			return new SchedulerService(influxService(), protocolController(), 
					yamlLoader().loadDatapoints(), applicationConfig());
		}
		return null;
	}
	
	public List<ValueFilter> filterChain() {
		List<ValueFilter> list = new ArrayList<>();
		list.add(new TemperatureFilter());
		
		return list;
	}
}
