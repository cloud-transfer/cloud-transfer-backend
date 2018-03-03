package com.github.dhaval_mehta.savetogoogledrive.model;

public class UploadInformation {

	private String url;
	private String fileName;
	private long totalSize;
	private long uploadedSize;
	private String errorMessage;
	private UploadStatus uploadStatus;
	private double speed;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getUploadedSize() {
		return uploadedSize;
	}

	public void setUploadedSize(long uploadedSize) {
		this.uploadedSize = uploadedSize;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}