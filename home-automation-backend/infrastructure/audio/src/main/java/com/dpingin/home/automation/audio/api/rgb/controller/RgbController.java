package com.dpingin.home.automation.audio.api.rgb.controller;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 17.07.14
 * Time: 23:03
 * To change this template use File | Settings | File Templates.
 */
public interface RgbController
{
    void init();
    void destroy();

    void start();
    void stop();
}
