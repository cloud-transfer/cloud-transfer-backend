package com.github.dhaval_mehta.savetogoogledrive.uploader;

import java.math.BigInteger;
import java.security.SecureRandom;

public class UploadTask implements Runnable {

    private static final SecureRandom random = new SecureRandom();
    private String id;
    private Uploader uploader;

    public UploadTask(Uploader uploader) {
        id = generateRandomId() + Integer.toString(hashCode(), 32);
        this.uploader = uploader;
    }

    private static synchronized String generateRandomId() {
        return new BigInteger(130, random).toString(32) + new BigInteger(String.valueOf(System.currentTimeMillis())).toString(32);
    }

    @Override
    public void run() {
        uploader.upload();
    }

    public String getId() {
        return id;
    }

    public Uploader getUploader() {
        return uploader;
    }
}
