package com.procoin.nsk.util;

import java.io.UnsupportedEncodingException;

import com.procoin.nsk.Protocol;
import com.procoin.nsk.exceptions.TjrNSKDataException;
import com.procoin.nsk.exceptions.TjrNSKException;


/**
 * The only reason to have this is to be able to compatible with java 1.5 :(
 * 
 */
public class SafeEncoder {
	public static byte[][] encodeMany(final String... strs){
		byte[][] many = new byte[strs.length][];
		for(int i=0;i<strs.length;i++){
			many[i] = encode(strs[i]);
		}
		return many;
	}
	
    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new TjrNSKDataException(
                        "value sent to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new TjrNSKException(e);
        }
    }

    public static String encode(final byte[] data) {
        try {
            return new String(data, Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new TjrNSKException(e);
        }
    }
}
