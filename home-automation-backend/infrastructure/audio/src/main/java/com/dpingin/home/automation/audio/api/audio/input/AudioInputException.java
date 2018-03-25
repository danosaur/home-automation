package com.dpingin.home.automation.audio.api.audio.input;

import com.dpingin.home.automation.commons.api.exception.HomeAutomationException;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 25.10.14
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public class AudioInputException extends HomeAutomationException
{
	public AudioInputException()
	{
	}

	public AudioInputException(String message)
	{
		super(message);
	}

	public AudioInputException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AudioInputException(Throwable cause)
	{
		super(cause);
	}

	public AudioInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
