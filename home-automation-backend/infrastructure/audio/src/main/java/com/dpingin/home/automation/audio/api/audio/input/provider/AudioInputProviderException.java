package com.dpingin.home.automation.audio.api.audio.input.provider;

import com.dpingin.home.automation.commons.api.exception.HomeAutomationException;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 23:01
 * To change this template use File | Settings | File Templates.
 */
public class AudioInputProviderException extends HomeAutomationException
{
	public AudioInputProviderException()
	{
	}

	public AudioInputProviderException(String message)
	{
		super(message);
	}

	public AudioInputProviderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AudioInputProviderException(Throwable cause)
	{
		super(cause);
	}

	public AudioInputProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
