/*
 * Copyright 2009-2010 MBTE Sweden AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.procoin.nsk.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.procoin.http.TjrBaseApi;
import com.procoin.nsk.exceptions.TjrNSKConnectionException;

public class TjrSocketInputStream extends FilterInputStream {

    protected final byte buf[];

    protected int count, limit;

    public TjrSocketInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buf = new byte[size];
    }

    public TjrSocketInputStream(InputStream in) {
        this(in, 8192);
    }

    public byte readByte() throws IOException {
        if (count == limit) {
            fill();
        }

        return buf[count++];
    }

    public String readLine() {
        int b;
        byte c;
        StringBuilder sb = new StringBuilder();

        try {
            while (true) {
                if (count == limit) {
                    fill();
                }
                if (limit == -1)
                    break;

                b = buf[count++];
                if (b == '\r') {
                    if (count == limit) {
                        fill();
                    }

                    if (limit == -1) {
                        sb.append((char) b);
                        break;
                    }

                    c = buf[count++];
                    if (c == '\n') {
                        break;
                    }
                    sb.append((char) b);
                    sb.append((char) c);
                } else {
                    sb.append((char) b);
                }
            }
        } catch (IOException e) {
            throw new TjrNSKConnectionException(e);
        }
        String reply = sb.toString();
        if(TjrBaseApi.isDebug)Log.d("Protocol", "==4==sendCommand...Exception:"+reply);
        if (reply.length() == 0) {
            throw new TjrNSKConnectionException(
                    "It seems like server has closed the connection.");
        }
        return reply;
    }

    public String readAllLine() {
		int b;
		byte c;
		byte[] dest = new byte[0]; 
		try {
			while (true) {
				if (count == limit) {
					fill();
					byte[] tmp = new byte[dest.length + limit]; 
					System.arraycopy(dest, 0, tmp, 0, dest.length); 
					System.arraycopy(buf, 0, tmp, dest.length, limit); 
		            dest = tmp; 
				}
				if (limit == -1) break;
				if(limit>1){
					b = buf[limit-2];
					if (b == '\r') {
						c = buf[limit-1];
						if (c == '\n') {
							count = limit;
							break;
						}
						count = limit;
						break;
					}else {
						count = limit;
					}
				}else {
					break;
				}
			}
		} catch (IOException e) {
			throw new TjrNSKConnectionException(e);
		}
		String reply = SafeEncoder.encode(dest);
		dest = null;
		if (reply.length() == 0) {
			throw new TjrNSKConnectionException("It seems like server has closed the connection.");
		}
		return reply;
	}
    
    
    public int read(byte[] b, int off, int len) throws IOException {
        if (count == limit) {
            fill();
            if (limit == -1)
                return -1;
        }
        final int length = Math.min(limit - count, len);
        System.arraycopy(buf, count, b, off, length);
        count += length;
        return length;
    }

    private void fill() throws IOException {
        limit = in.read(buf);
        count = 0;
    }
}
