/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Dhaval
 */
public class OAuthException extends RuntimeException
{

    private String error;
    private String description;

    public OAuthException()
    {
    }

    public OAuthException(String message)
    {
	super(message);
    }

    public OAuthException(Throwable cause)
    {
	super(cause);
    }

    public OAuthException(String message, Throwable cause)
    {
	super(message, cause);
    }

    public String getError()
    {
	return error;
    }

    public void setError(String error)
    {
	this.error = error;
    }

    public String getDescription()
    {
	return description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    @Override
    public String getMessage()
    {
	return "Error: " + error + ", " + "Description: " + description;
    }
}
