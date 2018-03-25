package com.dpingin.home.automation.rgb.controller.api.rgb;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 10.06.14
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public class RgbControllerException extends Exception
{
	public RgbControllerException(String message)
	{
		super(message);
	}

	public RgbControllerException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
