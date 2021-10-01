package com.procoin.subpush;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static ExecutorService executorService = null;

    static {
        threadingStart();
    }

    synchronized protected static void threadingStart() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
    }

//	synchronized protected static void threadingEnd() {
//		if(executorService != null)executorService.shutdown();
//		executorService = null;
//	}

    synchronized public static final void submit(final Runnable task) {
        if (!executorService.isTerminated() && !executorService.isShutdown() && task != null) {
            executorService.submit(task);
        }
    }
}

