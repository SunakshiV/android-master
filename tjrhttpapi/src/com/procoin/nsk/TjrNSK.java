package com.procoin.nsk;



public class TjrNSK {
	private Client client = null;

	public TjrNSK(final String host, final int port, final int timeout, final int soTimeout) {
		client = new Client(host, port);
		client.setTimeout(timeout);
		client.setSoTimeout(soTimeout);
	}

	public void connect() {
		client.connect();
	}

	public void disconnect() {
		client.disconnect();
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	protected String sendCommand(final String cmd) throws Exception {
		return client.sendCommand(cmd);
	}

	public String getHost(){
		return client.getHost();
	}
//	protected String sendCommand(final String cmd) throws Exception {
//		return client.sendCommand(cmd);
//		return client.getStatusCodeReply();
//	}
}
