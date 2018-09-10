package org.openlink.datapoint.scheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.openlink.datapoint.config.model.ApplicationConfig;
import org.openlink.datapoint.controller.CacheController;
import org.openlink.datapoint.controller.ProtocolController;
import org.openlink.datapoint.model.Datapoint;
import org.openlink.datapoint.protocol.command.DatapointOperationExecutor;
import org.openlink.datapoint.protocol.command.ReadDatapointOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.classic.Logger;

public class SchedulerService {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SchedulerService.class);
	
	@Autowired
	private DatapointOperationExecutor operationExecutor;
	
	@Autowired
	private ProtocolController protocolController;
	
	@Autowired
	private CacheController cache;
	
	@Autowired
	private ApplicationConfig config;
	
	@Autowired
	private List<Datapoint> datapoints;
	
	private ScheduledExecutorService executorService;
	
	@PostConstruct
	private void init() {
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
