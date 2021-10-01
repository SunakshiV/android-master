package com.procoin.nsk.exceptions;

public class TjrNSKConnectionException extends TjrNSKException {

	private static final long serialVersionUID = 9029252055844703208L;

	public TjrNSKConnectionException(String message) {
        super(message);
    }

    public TjrNSKConnectionException(Throwable cause) {
        super(cause);
    }

    public TjrNSKConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
