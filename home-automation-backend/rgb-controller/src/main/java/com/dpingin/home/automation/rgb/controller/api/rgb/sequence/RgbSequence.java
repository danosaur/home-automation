package com.dpingin.home.automation.rgb.controller.api.rgb.sequence;

import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:14
 * To change this template use File | Settings | File Templates.
 */
public interface RgbSequence
{
    String getName();

    void start();
    void stop();
}
