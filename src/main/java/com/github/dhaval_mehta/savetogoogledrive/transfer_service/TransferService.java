package com.github.dhaval_mehta.savetogoogledrive.transfer_service;

public interface TransferService extends Runnable {

    void transfer();

    default void run() {
        transfer();
    }
}
