package com.github.dhaval_mehta.cloud_transfer.transfer_service;

import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransferManager {

    private static TransferManager instance;

    static {
        instance = new TransferManager();
    }

    private Hashtable<String, TransferService> tasks;
    private ExecutorService executorService;

    private TransferManager() {
        tasks = new Hashtable<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    public static TransferManager getInstance() {
        return instance;
    }

    public String addTask(TransferService transferService) {
        String id = UUID.randomUUID().toString();
        tasks.put(id, transferService);
        executorService.submit(transferService);
        return id;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
