package com.procoin.nsk;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

import com.procoin.http.TjrBaseApi;

import android.util.Log;

public class TjrNSKPool extends Pool<TjrNSK> {

	public TjrNSKPool(final Config poolConfig, final String host, final int port, final int timeout,final int soTimeout) {
		super(poolConfig, new TjrNSKFactory(host, port, timeout,soTimeout));
	}

	public void returnBrokenResource(final TjrNSK resource) {
		returnBrokenResourceObject(resource);
	}

	public void returnResource(final TjrNSK resource) {
		returnResourceObject(resource);
	}

	/**
	 * PoolableObjectFactory custom impl.
	 */
	private static class TjrNSKFactory extends BasePoolableObjectFactory {
		private String host;
		private int port;
		private int timeout;
		private int soTimeout;

		public TjrNSKFactory( String host,  int port,  int timeout, int soTimeout) {
			super();
			this.host = host;
			this.port = port;
			this.timeout = timeout;
			this.soTimeout = soTimeout;
		}

		public Object makeObject() throws Exception {
			final TjrNSK tjrnsk = new TjrNSK(this.host, this.port, this.timeout, this.soTimeout);
			if(TjrBaseApi.isDebug)Log.d("Protocol", "socket makeObject..."+tjrnsk);
			tjrnsk.connect();
			return tjrnsk;
		}

		public void destroyObject(final Object obj) throws Exception {
			if (obj instanceof TjrNSK) {
				final TjrNSK tjrnsk = (TjrNSK) obj;
				try {
					if(TjrBaseApi.isDebug)Log.d("Protocol", "socket destroyObject..."+tjrnsk);
					tjrnsk.disconnect();
				} catch (Exception e) {

				}
			}
		}
		
		public boolean validateObject(final Object obj) {
			if (obj instanceof TjrNSK) {
				final TjrNSK tjrnsk = (TjrNSK) obj;
				try {
					if(TjrBaseApi.isDebug)Log.d("Protocol", "--1--validateObject..."+tjrnsk);
					String pong = tjrnsk.sendCommand(Protocol.PING);
					boolean suc = (tjrnsk.isConnected() && pong!=null);
					if(TjrBaseApi.isDebug)Log.d("Protocol", "--4--validateObject..."+tjrnsk+"  pong="+pong+"  suc="+suc);
					return suc;
				} catch (final Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}
	}
}
