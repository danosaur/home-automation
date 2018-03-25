package com.dpingin.home.automation.commons.api.exception;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 23:05
 * To change this template use File | Settings | File Templates.
 */
public class HomeAutomationException extends Exception
{
	public HomeAutomationException()
	{
	}

	public HomeAutomationException(String message)
	{
		super(message);
	}

	public HomeAutomationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public HomeAutomationException(Throwable cause)
	{
		super(cause);
	}

	public HomeAutomationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
