package com.dpingin.home.automation.pattern.api.control;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public interface Controls extends Map<String, Control>
{
	<T> T get(Class<T> controlClass, String name);
	void add(Control... controls);
}
