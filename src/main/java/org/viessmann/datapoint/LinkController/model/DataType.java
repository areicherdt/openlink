package org.viessmann.datapoint.LinkController.model;

public enum DataType {

	BOOL	(1, 1),
	BYTE	(1, 1),
	UBYTE	(1, 1),
	SHORT	(2, 1),
	USHORT	(2, 1),
	INT		(4, 1),
	UINIT	(4, 1),
	DATE	(8, 1),
	TIMER	(8, 1);
	
	private int length;
	private int divider;
	
	DataType(int length, int divider) {
		this.length = length;
		this.divider = divider;
	}

	public int getLength() {
		return length;
	}

	public int getDivider() {
		return divider;
	}
}
