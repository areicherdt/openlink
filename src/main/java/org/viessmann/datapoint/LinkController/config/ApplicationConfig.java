package org.viessmann.datapoint.LinkController.config;

import org.viessmann.datapoint.LinkController.config.model.Database;
import org.viessmann.datapoint.LinkController.config.model.Schedule;
import org.viessmann.datapoint.LinkController.protocol.ProtocolType;

public class ApplicationConfig {

	private String port;
	private ProtocolType protocol;
	private String description;
	private Schedule schedule;
	private Database database;
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public ProtocolType getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolType protocol) {
		this.protocol = protocol;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
}
