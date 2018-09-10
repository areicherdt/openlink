package org.openlink.datapoint.config;

import java.util.ArrayList;
import java.util.List;

import org.openlink.datapoint.config.model.ApplicationConfig;
import org.openlink.datapoint.config.model.Schedule;
import org.openlink.datapoint.controller.InterfaceController;
import org.openlink.datapoint.controller.ProtocolController;
import org.openlink.datapoint.model.Datapoint;
import org.openlink.datapoint.protocol.TemperatureFilter;
import org.openlink.datapoint.protocol.ValueFilter;
import org.openlink.datapoint.protocol.Viessmann300Protocol;
import org.openlink.datapoint.protocol.ViessmannKWProtocol;
import org.openlink.datapoint.scheduler.SchedulerService;
import org.openlink.datapoint.util.YamlLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.openlink.datapoint"})
public class SpringConfig {

	@Bean
	public ApplicationConfig applicationConfig() {
		return YamlLoader.loadConfiguration();
	}
	
	@Bean 
	List<Datapoint> datapointList() {
		return YamlLoader.loadDatapoints();
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
	public SchedulerService schedulerService() {
		Schedule scheduleConfig = applicationConfig().getSchedule();
		if(scheduleConfig.isEnabled()) {
			return new SchedulerService();
		}
		return null;
	}
	
	public List<ValueFilter> filterChain() {
		List<ValueFilter> list = new ArrayList<>();
		list.add(new TemperatureFilter());
		
		return list;
	}
}
