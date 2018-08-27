package org.viessmann.datapoint.LinkController.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.viessmann.datapoint.LinkController.model.Datapoint;

public class YamlTest {

	@Test
	public void testBaseYaml() {
		YamlLoader loader = new YamlLoader();
		List<Datapoint> list = loader.loadDatapointYamlConfiguration();
		
		assertTrue(!list.isEmpty());
		list.forEach(System.out::println);
	}
	
}
