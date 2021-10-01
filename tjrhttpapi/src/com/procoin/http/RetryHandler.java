package com.procoin.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import com.procoin.http.util.CommonUtil;

public class RetryHandler implements HttpRequestRetryHandler {

	/** the number of times a method will be retried */
	private final int retryCount;
	/**
	 * Whether or not methods that have successfully sent their request will be
	 * retried
	 */
	private final boolean requestSentRetryEnabled;

	/**
	 * Default constructor
	 */
	public RetryHandler(int retryCount, boolean requestSentRetryEnabled) {
		super();
		this.retryCount = retryCount;
		this.requestSentRetryEnabled = requestSentRetryEnabled;
	}

	/**
	 * Default constructor
	 */
	public RetryHandler(int retryCount) {
		this(retryCount, false);
	}

	/**
	 * Used <code>retryCount and requestSentRetryEnabled to determine
	 * if the given method should be retried.
	 */
	public boolean retryRequest(final IOException exception, int executionCount, final HttpContext context) {
		CommonUtil.LogLa(4, "-------------尝试再次重连-------------retryRequest:"+exception+" -------executionCount:"+executionCount);
		if (exception == null) {
			throw new IllegalArgumentException("Exception parameter may not be null");
		}
		if (context == null) {
			throw new IllegalArgumentException("HTTP context may not be null");
		}
		if (executionCount > this.retryCount) {
			// Do not retry if over max retry count
			return false;
		}
		if (exception instanceof NoHttpResponseException) {
			// Retry if the server dropped connection on us
			return true;
		}
		if (exception instanceof UnknownHostException) {
			// retry-this, since it may happens as part of a Wi-Fi to 3G
			// failover
			return true;
		}
		if (exception instanceof SocketException) {
			// retry-this, since it may happens as part of a Wi-Fi to 3G
			// failover
			return true;
		}
		if (exception instanceof InterruptedIOException) {
			// Timeout
			return false;
		}
		if (exception instanceof ConnectException) {
			// Connection refused
			return false;
		}
		if (exception instanceof SSLHandshakeException) {
			// SSL handshake exception
			return false;
		}

		HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
		boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
		if (idempotent) {
			// Retry if the request is considered idempotent
			return true;
		}

		Boolean b = (Boolean) context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
		boolean sent = (b != null && b.booleanValue());

		if (!sent || this.requestSentRetryEnabled) {
			// Retry if the request has not been sent fully or
			// if it's OK to retry methods that have been sent
			return true;
		}
		// otherwise do not retry
		return false;
	}
}
