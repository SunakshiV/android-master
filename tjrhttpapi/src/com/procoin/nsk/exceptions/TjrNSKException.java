package com.procoin.nsk.exceptions;


public class TjrNSKException extends RuntimeException {
	
	private static final long serialVersionUID = -6611116520257619881L;

	public TjrNSKException(String message) {
        super(message);
    }

    public TjrNSKException(Throwable e) {
        super(e);
    }

    public TjrNSKException(String message, Throwable cause) {
        super(message, cause);
    }
}
