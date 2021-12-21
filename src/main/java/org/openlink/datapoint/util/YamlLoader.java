package org.openlink.datapoint.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openlink.datapoint.config.model.ApplicationConfig;
import org.openlink.datapoint.model.Datapoint;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import ch.qos.logback.classic.Logger;

public class YamlLoader {
	
	public final static String CONFIG_PATH = "/var/opt/openlink/";
	
	static Logger logger = (Logger) LoggerFactory.getLogger(YamlLoader.class);

	public static ApplicationConfig loadConfiguration() {

		Yaml yaml = new Yaml(new Constructor(ApplicationConfig.class));
		InputStream io = loadInputStream("base.yaml");
		ApplicationConfig config = yaml.load(io);
		try {
			io.close();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
		return config;
	}
	
	public static List<Datapoint> loadDatapoints() {
		
		List<Datapoint> result = new ArrayList<>();
		Yaml yaml = new Yaml(new Constructor(Datapoint.class));
		InputStream io = loadInputStream("datapoints.yaml");
		yaml.loadAll(io).forEach(o -> result.add((Datapoint) o));
		try {
			io.close();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("resource")
	private static InputStream loadInputStream(String filename) {
		
		InputStream io = null;
		try {
			File file = new File(CONFIG_PATH+"/"+filename);		
			io = new FileInputStream(file);
			logger.debug("loaded config {}", file.getPath());
		} catch (IOException e) {
			logger.warn("config {} not found, using default settings", filename);
			io = YamlLoader.class
					  .getClassLoader()
					  .getResourceAsStream(filename);
		}
		
		return io;
	}
}
