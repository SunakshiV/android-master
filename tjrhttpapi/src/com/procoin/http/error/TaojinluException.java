package com.procoin.http.error;

public class TaojinluException extends Exception{
private static final long serialVersionUID = 1L;
    
    private String mExtra;

    public TaojinluException(String message) {
        super(message);
    }

    public TaojinluException(String message, String extra) {
        super(message);
        mExtra = extra;
    }
    
    public String getExtra() {
        return mExtra;
    }
}
