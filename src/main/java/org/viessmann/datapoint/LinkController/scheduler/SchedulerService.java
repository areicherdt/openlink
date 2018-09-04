package org.viessmann.datapoint.LinkController.scheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.config.ApplicationConfig;
import org.viessmann.datapoint.LinkController.controller.CacheController;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.model.Datapoint;
import org.viessmann.datapoint.LinkController.protocol.command.DatapointOperationExecutor;
import org.viessmann.datapoint.LinkController.protocol.command.ReadDatapointOperation;

import ch.qos.logback.classic.Logger;

public class SchedulerService {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SchedulerService.class);

	private DatapointOperationExecutor operationExecutor;
	private ScheduledExecutorService executorService;
	private ProtocolController protocolController;
	private CacheController cache;
	
	private ApplicationConfig config;
	private List<Datapoint> datapoints;
	
	public SchedulerService(List<Datapoint> datapoints, ApplicationConfig config, DatapointOperationExecutor operationExecutor,
			ProtocolController protocolController, CacheController cache) {
		 
		executorService = Executors.newSingleThreadScheduledExecutor();
		
		this.protocolController = protocolController;
		this.cache = cache;
		this.operationExecutor = operationExecutor;
		this.config = config;
		this.datapoints = datapoints;
		
		logger.debug("scheduler created.");
		startExecutor();
	}
	
	public void startExecutor() {
		executorService.scheduleAtFixedRate(createScheduledTask()
				, 1, config.getSchedule().getCronInMinutes(), TimeUnit.MINUTES);
		
		logger.info("scheduler started with {} min period.", config.getSchedule().getCronInMinutes());
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
				datapoints.stream().filter(Datapoint::isCache).forEach( datapoint -> {
					ReadDatapointOperation operation = new ReadDatapointOperation(protocolController, cache, datapoint);
					operationExecutor.addTask(operation);
				});				
			}
		};
	}
}
