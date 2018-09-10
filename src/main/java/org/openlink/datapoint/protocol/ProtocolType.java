package org.openlink.datapoint.protocol;

public enum ProtocolType {

	V300("300"),
	KW("kw");
	
	private final String name;
	
	private ProtocolType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
