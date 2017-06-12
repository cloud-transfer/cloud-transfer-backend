/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Dhaval
 */
public class OAuthError
{

    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;

    public String getError()
    {
	return error;
    }

    public void setError(String error)
    {
	this.error = error;
    }

    public String getErrorDescription()
    {
	return errorDescription;
    }

    public void setErrorDescription(String errorDescription)
    {
	this.errorDescription = errorDescription;
    }

    @Override
    public String toString()
    {
	return "OAuthError{" + "error=" + error + ", errorDescription=" + errorDescription + '}';
    }

}
