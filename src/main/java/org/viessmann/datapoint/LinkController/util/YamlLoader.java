package org.viessmann.datapoint.LinkController.util;

import java.io.InputStream;


import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.config.ApplicationConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import ch.qos.logback.classic.Logger;

public class YamlLoader {
	
	Logger logger = (Logger) LoggerFactory.getLogger(YamlLoader.class);

	public ApplicationConfig loadYamlConfiguration() {
		
		Yaml yaml = new Yaml(new Constructor(ApplicationConfig.class));
		
		InputStream inputStream = this.getClass()
				  .getClassLoader()
				  .getResourceAsStream("base.yaml");
			
		ApplicationConfig config = yaml.load(inputStream);
		
		return config;
	}
}
