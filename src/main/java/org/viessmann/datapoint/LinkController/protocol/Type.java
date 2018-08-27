package org.viessmann.datapoint.LinkController.protocol;

public enum Type {

	BOOL(1),
	BYTE(1),
	UBYTE(1),
	SHORT(2),
	USHORT(2),
	INT(4),
	UINIT(4),
	DATE(8),
	TIMER(8);
	
	private int len;
	
	Type(int len) {
		this.len = len;
	}

	public int getLen() {
		return len;
	}

}
