package org.openlink.datapoint.protocol.command;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class DatapointOperationExecutor {
	
	ThreadPoolExecutor executor;
	
	public DatapointOperationExecutor() {
		this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
	}
	
	public Future<String> addTask(DatapointOperation operation) {	
		return executor.submit(operation::execute);
	}
}
