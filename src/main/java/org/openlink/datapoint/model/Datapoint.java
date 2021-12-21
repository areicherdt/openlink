package org.openlink.datapoint.model;

public class Datapoint {

	private String address;
	private DataType type;
	private String channel;
	private boolean cache;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}	
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(address).append(" / ").append(type).append(" / ").append(channel).append(" / cache: ").append(cache);
		return str.toString();
	}
}
