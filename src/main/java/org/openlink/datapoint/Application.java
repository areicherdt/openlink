package org.openlink.datapoint;

import java.time.Instant;

import org.openlink.datapoint.config.SparkConfig;
import org.openlink.datapoint.config.SpringConfig;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.qos.logback.classic.Logger;

public class Application {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		
		logger.info("Application startup.");
		SparkConfig.load();	
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
		logger.info("context loaded {}", Instant.ofEpochMilli(ctx.getStartupDate()));
	}

}
