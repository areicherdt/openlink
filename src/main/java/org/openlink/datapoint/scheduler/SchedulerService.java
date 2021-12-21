package org.openlink.datapoint.scheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openlink.datapoint.config.model.ApplicationConfig;
import org.openlink.datapoint.controller.CacheController;
import org.openlink.datapoint.controller.ProtocolController;
import org.openlink.datapoint.model.Datapoint;
import org.openlink.datapoint.protocol.command.DatapointOperationExecutor;
import org.openlink.datapoint.protocol.command.ReadDatapointOperation;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class SchedulerService {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SchedulerService.class);
	
	private final DatapointOperationExecutor operationExecutor;
	private final ProtocolController protocolController;
	private final CacheController cache;
	private final ApplicationConfig config;
	private final List<Datapoint> datapoints;
	private final ScheduledExecutorService executorService;

	public SchedulerService(ApplicationConfig config,
							CacheController cache,
							DatapointOperationExecutor operationExecutor,
							ProtocolController protocolController,
							List<Datapoint> datapoints)
	{
		this.datapoints = datapoints;
		this.config = config;
		this.cache = cache;
		this.operationExecutor = operationExecutor;
		this.protocolController = protocolController;

		executorService = Executors.newSingleThreadScheduledExecutor();
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
				Thread.currentThread().setName("SchedulerService");
				logger.debug("execute scheduled task.");
				datapoints.stream().filter(Datapoint::isCache).forEach( datapoint -> {
					ReadDatapointOperation operation = new ReadDatapointOperation(protocolController, cache, datapoint);
					operationExecutor.addTask(operation);
				});				
			}
		};
	}
}
