package com.example.librarymanagement.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Central thread manager used by SQLite, network and Firebase fallback operations.
 * This demonstrates the Threads topic without blocking the Android main thread.
 */
public final class AppExecutors {
    private static volatile AppExecutors instance;

    private final ExecutorService diskIO = Executors.newSingleThreadExecutor();
    private final ExecutorService networkIO = Executors.newFixedThreadPool(3);
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    private AppExecutors() {
    }

    public static AppExecutors getInstance() {
        if (instance == null) {
            synchronized (AppExecutors.class) {
                if (instance == null) {
                    instance = new AppExecutors();
                }
            }
        }
        return instance;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }

    public ExecutorService networkIO() {
        return networkIO;
    }

    public void runOnMainThread(Runnable runnable) {
        mainThread.post(runnable);
    }
}
