package org.viessmann.datapoint.LinkController.config;

import org.viessmann.datapoint.LinkController.protocol.ProtocolType;

public class ApplicationConfig {

	private String port;
	private ProtocolType protocol;
	private String description;

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
}
