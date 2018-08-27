package org.viessmann.datapoint.LinkController.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.model.Datapoint;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import ch.qos.logback.classic.Logger;

public class YamlLoader {
	
	Logger logger = (Logger) LoggerFactory.getLogger(YamlLoader.class);

	public List<Datapoint> loadDatapointYamlConfiguration() {
		
		Yaml yaml = new Yaml(new Constructor(Datapoint.class));
		
		InputStream inputStream = this.getClass()
				  .getClassLoader()
				  .getResourceAsStream("base.yaml");
			
		List<Datapoint> resultList = new ArrayList<>();
		
		for(Object data: yaml.loadAll(inputStream)) {
			if(data instanceof Datapoint ) {
				resultList.add((Datapoint) data);
			} else {
				logger.warn("none datapoint object found in base.yaml - {}", data.toString());
			}
		}
		
		return resultList;
	}
}
