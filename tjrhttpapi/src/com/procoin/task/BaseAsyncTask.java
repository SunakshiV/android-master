package com.procoin.task;

import android.os.AsyncTask;
import android.os.Build;

/**
 * 
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	public final AsyncTask<Params, Progress, Result> executeParams(Params... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
		} else {
			return this.execute(params);
		}
	}
}
