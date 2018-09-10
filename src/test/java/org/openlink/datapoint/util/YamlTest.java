package org.openlink.datapoint.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openlink.datapoint.config.model.ApplicationConfig;
import org.openlink.datapoint.model.Datapoint;
import org.openlink.datapoint.protocol.ProtocolType;
import org.openlink.datapoint.util.YamlLoader;

public class YamlTest {

	@Test
	public void testBaseYaml() {
		ApplicationConfig config = YamlLoader.loadConfiguration();
		
		assertNotNull(config);
		assertTrue(!config.getPort().isEmpty());
		assertEquals(ProtocolType.KW, config.getProtocol());
		
		assertTrue(config.getSchedule().isEnabled());
	}
	
	@Test
	public void testDatapointYaml() {
		List<Datapoint> list = YamlLoader.loadDatapoints();
		
		assertTrue(list.size() > 1);
		list.forEach(dp -> assertTrue(!dp.getAddress().isEmpty()));
	}
}
