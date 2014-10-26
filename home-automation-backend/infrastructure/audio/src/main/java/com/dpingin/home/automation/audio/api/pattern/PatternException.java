package com.dpingin.home.automation.audio.api.pattern;

import com.dpingin.home.automation.commons.api.exception.HomeAutomationException;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 25.10.14
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class PatternException extends HomeAutomationException
{

    public PatternException()
    {
    }

    public PatternException(String message)
    {
        super(message);
    }

    public PatternException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PatternException(Throwable cause)
    {
        super(cause);
    }

    public PatternException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
