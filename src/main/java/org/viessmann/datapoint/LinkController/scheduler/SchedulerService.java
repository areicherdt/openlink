package org.viessmann.datapoint.LinkController.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.config.model.Logging;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.db.InfluxService;
import org.viessmann.datapoint.LinkController.model.Datapoint;

import ch.qos.logback.classic.Logger;

public class SchedulerService {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SchedulerService.class);

	private ScheduledExecutorService executorService;
	
	private InfluxService databaseService;
	private ProtocolController protocolController;
	
	private Logging logConfig;
	private List<Datapoint> datapoints;
	
	public SchedulerService(InfluxService influxService, 
			ProtocolController protocolController, List<Datapoint> datapoints, Logging logConfig) {
		this.logConfig = logConfig;
		this.datapoints = datapoints;
		this.databaseService = influxService;
		this.protocolController = protocolController;
		executorService = Executors.newSingleThreadScheduledExecutor();
		logger.debug("scheduler created.");
		startExecutor();
	}
	
	public void startExecutor() {
		executorService.scheduleAtFixedRate(createScheduledTask()
				, 1, logConfig.getCronInMinutes(), TimeUnit.MINUTES);
		
		logger.info("scheduler started with {} min period.", logConfig.getCronInMinutes());
	}
	
	public void stopExecutor() {
		executorService.shutdown();
		try {
		    if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
		        executorService.shutdownNow();
		    } 
		} catch (InterruptedException e) {
		    executorService.shutdownNow();
		}
	}
	
	private Runnable createScheduledTask() {
		return new Runnable() {	
			@Override
			public void run() {
				logger.debug("execute scheduled task.");
				datapoints.stream().filter(Datapoint::isLog).forEach( dp -> {				
					Map<String, Object> fieldMap = new HashMap<>();
					try {
						Object result = protocolController.readAddress(dp.getAddress(), dp.getType());
						fieldMap.put("value", result);
						databaseService.writeDataPoint(dp.getChannel(), fieldMap);
					} catch (Exception e) {
						logger.error("error while reading scheduled {}", e.getMessage());
					}
				});				
			}
		};
	}
}
