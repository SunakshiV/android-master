package com.procoin.nsk.exceptions;

public class TjrNSKDataException extends TjrNSKException {
	
	private static final long serialVersionUID = -6287675196183815497L;
	
	public TjrNSKDataException(String message) {
        super(message);
    }

    public TjrNSKDataException(Throwable cause) {
        super(cause);
    }

    public TjrNSKDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
