package com.dpingin.home.automation.rgb.controller.api.rgb;

import com.dpingin.home.automation.rgb.controller.api.color.Color;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 10.06.14
 * Time: 20:20
 * To change this template use File | Settings | File Templates.
 */
public interface RgbController
{
    Color getColor();
    void setColor(Color color) throws RgbControllerException;
    void setColor(int r, int g, int b) throws RgbControllerException;
}
