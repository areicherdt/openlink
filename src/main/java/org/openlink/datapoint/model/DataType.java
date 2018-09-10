package org.openlink.datapoint.model;

public enum DataType {

	TEMP10  (2, 10),
	TEMP100 (2, 100),
	OIL     (4, 1000),
	BOOL	(1, 1),
	SHORT	(2, 1),
	DATE	(8, 1),
	BYTE2   (2, 1),
	BYTE    (1, 1);
	
//	BYTE	(1, 1),
//	UBYTE	(1, 1),
//	USHORT	(2, 1),
//	INT		(4, 1),
//	UINIT	(4, 1),
//	TIMER	(8, 1);
	
	private final int length;
	private final int divider;
	
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
