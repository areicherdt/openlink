package org.viessmann.datapoint.LinkController;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.viessmann.datapoint.LinkController.config.SpringConfig;
import org.viessmann.datapoint.LinkController.config.SparkConfig;

import ch.qos.logback.classic.Logger;


public class Application {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		
		logger.debug("Application startup.");
		SparkConfig.load();	
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
		
		logger.debug("context loaded");
		
	}

}
