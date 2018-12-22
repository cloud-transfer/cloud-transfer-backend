package com.github.cloud_transfer.cloud_transfer.transfer_service;

import com.github.cloud_transfer.cloud_transfer.downloader.URLDownloader;
import com.github.cloud_transfer.cloud_transfer.model.DownloadInformation;
import com.github.cloud_transfer.cloud_transfer.model.TransferStatus;
import com.github.cloud_transfer.cloud_transfer.uploader.Uploader;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public abstract class TransferService implements Runnable {

    protected DownloadInformation downloadInformation;


    public void run() {
        transfer();
    }

    protected abstract @NotNull Uploader getUploader();

    protected abstract URLDownloader getDownloader() throws IOException;

    protected void transfer(){
        try {
            changeStatus(TransferStatus.Downloading);
            var downloader = getDownloader();
            downloadInformation = downloader.download();
            changeStatus(TransferStatus.Uploading);
            var uploader = getUploader();
            uploader.upload();
            changeStatus(TransferStatus.Finished);
        } catch (Exception e){
            changeStatus(TransferStatus.Failed);
        }
    }

    private void changeStatus(TransferStatus status){
        System.out.println(status);
    }



}
