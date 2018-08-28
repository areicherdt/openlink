package org.viessmann.datapoint.LinkController.config.model;

public class Database {
	
	private String url;
	private String user;
	private String password;
	private String retention;
	private String dbname;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRetention() {
		return retention;
	}
	public void setRetention(String retention) {
		this.retention = retention;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
}