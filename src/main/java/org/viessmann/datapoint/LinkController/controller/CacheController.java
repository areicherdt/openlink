package org.viessmann.datapoint.LinkController.controller;

import java.util.HashMap;
import java.util.Map;

import org.viessmann.datapoint.LinkController.model.ChannelResult;

public class CacheController {
	
	private Map<String, ChannelResult> cache;
	
	public CacheController() {
		this.cache = new HashMap<>();
	}
	
	public void put(String channel, String result) {
		cache.put(channel, new ChannelResult(result));
	}
	
	public void remove(String channel) {
		cache.remove(channel);
	}
	
	public Map<String, ChannelResult> readAll() {
		return cache;
	}
	
	public ChannelResult read(String channel) {
		return cache.get(channel);
	}
}
