package org.viessmann.datapoint.LinkController.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.viessmann.datapoint.LinkController.config.ApplicationConfig;
import org.viessmann.datapoint.LinkController.model.Datapoint;
import org.viessmann.datapoint.LinkController.protocol.ProtocolType;

public class YamlTest {

	@Test
	public void testBaseYaml() {
		YamlLoader loader = new YamlLoader();
		ApplicationConfig config = loader.loadConfiguration();
		
		assertNotNull(config);
		assertTrue(!config.getPort().isEmpty());
		assertEquals(ProtocolType.KW, config.getProtocol());
		
		assertTrue(config.getDatabase().getUrl().isEmpty());
		assertTrue(!config.getLogging().isEnabled());
	}
	
	@Test
	public void testDatapointYaml() {
		YamlLoader loader = new YamlLoader();
		List<Datapoint> list = loader.loadDatapoints();
		
		assertTrue(list.size() > 1);
		list.forEach(dp -> assertTrue(!dp.getAddress().isEmpty()));
	}
}
