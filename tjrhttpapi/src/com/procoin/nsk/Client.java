package com.procoin.nsk;


public class Client extends ConnectionNetty {

	public Client(final String host) {
		super(host);
	}
	
	public Client(final String host, final int port) {
		super(host, port);
	}

}
