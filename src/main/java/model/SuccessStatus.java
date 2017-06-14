/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dhaval
 */
public class SuccessStatus extends Status
{

    private double completePercentage;
    private String processingStatus;

    public SuccessStatus()
    {
    }

    public double getCompletePercentage()
    {
	return completePercentage;
    }

    public void setCompletePercentage(double completePercentage)
    {
	this.completePercentage = completePercentage;
    }

    public String getProcessingStatus()
    {
	return processingStatus;
    }

    public void setProcessingStatus(String processingStatus)
    {
	this.processingStatus = processingStatus;
    }

}
