package com.dpingin.home.automation.audio.api.pattern.control;

import com.dpingin.home.automation.audio.impl.pattern.control.FloatControl;
import org.springframework.expression.TypedValue;

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
}
