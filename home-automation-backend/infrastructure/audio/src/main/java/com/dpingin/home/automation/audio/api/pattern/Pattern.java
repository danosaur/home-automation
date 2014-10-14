package com.dpingin.home.automation.audio.api.pattern;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 02:12
 * To change this template use File | Settings | File Templates.
 */
public interface Pattern
{
    void init();
    void destroy();

    void start();
    void stop();

    String getName();
}
