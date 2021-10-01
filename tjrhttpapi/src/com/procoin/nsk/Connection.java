package com.procoin.nsk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicLong;

import android.util.Log;

import com.procoin.nsk.util.TjrSocketInputStream;
import com.procoin.http.TjrBaseApi;
import com.procoin.nsk.exceptions.TjrNSKConnectionException;
import com.procoin.nsk.exceptions.TjrNSKException;
import com.procoin.nsk.util.TjrSocketOutputStream;

public class Connection {
	private String host;
	private int port = Protocol.DEFAULT_PORT;
	private Socket socket;
	private TjrSocketOutputStream outputStream;
	private TjrSocketInputStream inputStream;
	private int timeout = Protocol.DEFAULT_TIMEOUT;
    private int soTimeout = Protocol.DEFAULT_SOTIMEOUT;
    private final AtomicLong transferredMessages = new AtomicLong();
    
    public Socket getSocket() {
		return socket;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public void setTimeoutInfinite() {
		try {
			if (!isConnected()) {
				connect();
			}
			socket.setKeepAlive(true);
			socket.setSoTimeout(0);
		} catch (SocketException ex) {
			throw new TjrNSKException(ex);
		}
	}

	public void rollbackTimeout() {
		try {
			socket.setSoTimeout(timeout);
			socket.setKeepAlive(false);
		} catch (SocketException ex) {
			throw new TjrNSKException(ex);
		}
	}
	
	protected void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new TjrNSKConnectionException(e);
        }
    }

	public Connection(final String host) {
		super();
		this.host = host;
	}

	public Connection(final String host, final int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public void connect() {
        if (!isConnected()) {
            try {
                socket = new Socket();
                //->@wjw_add
                socket.setReuseAddress(true);
                socket.setKeepAlive(true);  //Will monitor the TCP connection is valid
                socket.setTcpNoDelay(true);  //Socket buffer Whetherclosed, to ensure timely delivery of data
                socket.setSoLinger(true,0);  //Control calls close () method, the underlying socket is closed immediately
                //<-@wjw_add

                socket.connect(new InetSocketAddress(host, port), timeout);
                socket.setSoTimeout(soTimeout);
                outputStream = new TjrSocketOutputStream(socket.getOutputStream());
                inputStream = new TjrSocketInputStream(socket.getInputStream());
            } catch (IOException ex) {
                throw new TjrNSKConnectionException(ex);
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                throw new TjrNSKConnectionException(ex);
            }
        }
    }
    
    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
                && socket.isConnected() && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }
    
    protected String sendCommand(final String cmd) throws Exception{
        connect();
        try {
        	long msgId = transferredMessages.incrementAndGet();
    		if (!"".equals(cmd) && cmd.startsWith("/?")) {
    			outputStream.writeUtf8CrLf(cmd + "&msgId=" + msgId);
    		} else {
    			outputStream.writeUtf8CrLf("/?msgId=" + msgId );
    		}
    	} catch (IOException e) {
    	    throw new TjrNSKConnectionException(e);
    	}
        return getStatusCodeReply();
    }
    
    protected String getStatusCodeReply() {
        flush();
        try {
        	long msgId = transferredMessages.get();
        	String rex = msgId + Protocol.DOLLAR_BYTE;
        	String message = inputStream.readAllLine();
        	if(TjrBaseApi.isDebug)Log.d("Protocol", "==message=="+message);
        	if (message.startsWith(rex)) {
				return message.substring(rex.length());
			}else {
				 throw new TjrNSKConnectionException("It seems like server has closed the connection.");
			}
    	} catch (Exception e) {
    	    throw new TjrNSKConnectionException(e);
    	}
    }
}
