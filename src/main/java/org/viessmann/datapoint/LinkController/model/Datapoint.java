package org.viessmann.datapoint.LinkController.model;

public class Datapoint {

	private String address;
	private String description;
	private String type;
	private String channel;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(address).append(" / ").append(description).append(" / ")
			.append(type).append(" / ").append(channel);
		return str.toString();
	}
}
