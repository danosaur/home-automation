package com.dpingin.home.automation.audio.impl.pattern.control;

import com.dpingin.home.automation.audio.api.pattern.control.AbstractControl;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class FloatControl extends AbstractControl<Float>
{
	public FloatControl name(final String name)
	{
		this.name = name;
		return this;
	}

	public FloatControl value(final Float value)
	{
		this.value = value;
		return this;
	}

	public FloatControl minimumValue(final Float minimumValue)
	{
		this.minimumValue = minimumValue;
		return this;
	}

	public FloatControl maximumValue(final Float maximumValue)
	{
		this.maximumValue = maximumValue;
		return this;
	}

	public FloatControl defaultValue(final Float defaultValue)
	{
		this.defaultValue = defaultValue;
		return this;
	}
}
