package com.github.cloud_transfer.cloud_transfer_backend.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

public class ProgressBufferedInputStream extends BufferedInputStream {

    private long completed = 0;
    private SpeedMeter speedMeter = new SpeedMeter();

    public ProgressBufferedInputStream(InputStream in) {
        super(in);
    }

    public ProgressBufferedInputStream(InputStream in, int size) {
        super(in, size);
    }

    @Override
    public synchronized int read() throws IOException {
        completed++;
        return super.read();
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int length = super.read(b, off, len);
        completed += length;
        return length;
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        byte[] result = super.readNBytes(len);
        completed += result.length;
        return result;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        byte[] result = super.readAllBytes();
        completed += result.length;
        return result;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int result = super.read(b);
        completed += result;
        return result;
    }

    public double currentSpeed() {
        return speedMeter.currentSpeed();
    }

    private class SpeedMeter extends TimerTask {

        private double prevCompleted;
        private long prevExecutionTime;
        private double currentSpeed;

        @Override
        public void run() {
            if (completed - prevCompleted == 0)
                return;
            long currentTime = System.currentTimeMillis();
            currentSpeed = (completed - prevCompleted) / ((double) (currentTime - prevExecutionTime) / 1000);
            prevExecutionTime = currentTime;
            prevCompleted = completed;
        }

        double currentSpeed() {
            return currentSpeed;
        }
    }
}
