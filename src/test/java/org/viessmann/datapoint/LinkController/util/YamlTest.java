package org.viessmann.datapoint.LinkController.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.viessmann.datapoint.LinkController.config.ApplicationConfig;

public class YamlTest {

	@Test
	public void testBaseYaml() {
		YamlLoader loader = new YamlLoader();
		ApplicationConfig config = loader.loadYamlConfiguration();
		
		assertNotNull(config);
		assertEquals("/dev/ttyUSB0", config.getPort());
	}
	
}
