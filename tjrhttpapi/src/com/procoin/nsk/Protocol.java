package com.procoin.nsk;


public final class Protocol {
	public static final String PING = "";//空值
	public static final String PONG = "{}";
	public static final int DEFAULT_PORT = 8220;
	public static final int DEFAULT_TIMEOUT = 2000;
	public static final int DEFAULT_SOTIMEOUT = 5000;
	public static final String DOLLAR_BYTE = "$";
	public static final String CHARSET = "UTF-8";
	
	private Protocol() {
		// this prevent the class from instantiation
	}

}
