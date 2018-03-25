package com.dpingin.home.automation.pattern.api;

import com.dpingin.home.automation.pattern.api.control.Control;
import com.dpingin.home.automation.pattern.api.control.Controls;
import com.dpingin.home.automation.rgb.controller.api.color.Color;

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

	void start() throws PatternException;

	void stop();

	String getName();

	Controls getControls();

	void setControls(Controls controls);

	void updateControls(Controls controls);

	void updateControls(Collection<? extends Control> controls);

	Color getColor();

	void setColor(Color color);
}
