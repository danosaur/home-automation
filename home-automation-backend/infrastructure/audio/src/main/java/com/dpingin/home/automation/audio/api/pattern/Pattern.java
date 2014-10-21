package com.dpingin.home.automation.audio.api.pattern;

import com.dpingin.home.automation.audio.api.pattern.control.Control;
import com.dpingin.home.automation.audio.api.pattern.control.Controls;

import java.util.Collection;

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

    Controls getControls();
    void setControls(Controls controls);

    void updateControls(Controls controls);
    void updateControls(Collection<? extends Control> controls);
}
