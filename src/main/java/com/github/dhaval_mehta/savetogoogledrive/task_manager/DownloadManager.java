package com.github.dhaval_mehta.savetogoogledrive.task_manager;

import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloadManager {

    private static DownloadManager instance;

    static {
        instance = new DownloadManager();
    }

    private Hashtable<String, DownloadTask> tasks;
    private ExecutorService executorService;

    private DownloadManager() {
        tasks = new Hashtable<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    public static DownloadManager getInstance() {
        return instance;
    }

    public Future<?> addTask(DownloadTask task) {
        tasks.put(UUID.randomUUID().toString(), task);
        return executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
