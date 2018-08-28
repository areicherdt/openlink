package org.viessmann.datapoint.LinkController.config.model;

public class Logging {

	private boolean enabled;
	private int cronInMinutes;
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getCronInMinutes() {
		return cronInMinutes;
	}
	public void setCronInMinutes(int cronInMinutes) {
		this.cronInMinutes = cronInMinutes;
	}
}
