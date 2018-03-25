package com.dpingin.home.automation.pattern.api.switcher;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 04:50
 * To change this template use File | Settings | File Templates.
 */
public class PattenSwitcherException extends Exception
{
	public PattenSwitcherException(String message)
	{
		super(message);
	}

	public PattenSwitcherException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PattenSwitcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
