package com.github.cloud_transfer.cloud_transfer_backend.transfer_service;

import com.github.cloud_transfer.cloud_transfer_backend.downloader.URLDownloader;
import com.github.cloud_transfer.cloud_transfer_backend.model.DownloadInformation;
import com.github.cloud_transfer.cloud_transfer_backend.model.SpeedMeter;
import com.github.cloud_transfer.cloud_transfer_backend.model.TransferStatus;
import com.github.cloud_transfer.cloud_transfer_backend.uploader.Uploader;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public abstract class TransferService implements Runnable, SpeedMeter {

    DownloadInformation downloadInformation;
    private SpeedMeter speedMeter;

    public void run() {
        transfer();
    }

    protected abstract @NotNull Uploader getUploader();

    protected abstract URLDownloader getDownloader() throws IOException;

    private void transfer() {
        try {
            download();
            upload();
            changeStatusTo(TransferStatus.Finished);
        } catch (Exception e) {
            changeStatusTo(TransferStatus.Failed);
        }
    }

    private void download() throws IOException, InterruptedException {
        var downloader = getDownloader();
        speedMeter = downloader;
        changeStatusTo(TransferStatus.Downloading);
        downloadInformation = downloader.download();
    }

    private void upload() throws IOException, InterruptedException {
        var uploader = getUploader();
        speedMeter = uploader;
        changeStatusTo(TransferStatus.Uploading);
        uploader.upload();
    }

    private void changeStatusTo(TransferStatus status) {
        System.out.println(status);
    }

    @Override
    public double getCurrentSpeed() {

        if (speedMeter == null)
            return 0;

        return speedMeter.getCurrentSpeed();
    }
}
