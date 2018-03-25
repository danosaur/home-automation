package com.dpingin.home.automation.rgb.controller.api.rs232;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.11.13
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public class RS232ControllerException extends Exception
{
	public RS232ControllerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RS232ControllerException(String message)
	{
		super(message);
	}
}
