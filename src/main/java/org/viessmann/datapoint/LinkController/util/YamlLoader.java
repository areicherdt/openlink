package org.viessmann.datapoint.LinkController.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.config.ApplicationConfig;
import org.viessmann.datapoint.LinkController.model.Datapoint;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import ch.qos.logback.classic.Logger;

public class YamlLoader {
	
	Logger logger = (Logger) LoggerFactory.getLogger(YamlLoader.class);

	public ApplicationConfig loadConfiguration() {
		
		Yaml yaml = new Yaml(new Constructor(ApplicationConfig.class));
		
		InputStream inputStream = this.getClass()
				  .getClassLoader()
				  .getResourceAsStream("base.yaml");
			
		ApplicationConfig config = yaml.load(inputStream);
		
		return config;
	}
	
	public List<Datapoint> loadDatapoints() {
		
		List<Datapoint> result = new ArrayList<>();
		Yaml yaml = new Yaml(new Constructor(Datapoint.class));
		
		InputStream inputStream = this.getClass()
					.getClassLoader()
					.getResourceAsStream("datapoints.yaml");
		
		yaml.loadAll(inputStream).forEach(o -> result.add((Datapoint) o));
		return result;
	}
}
