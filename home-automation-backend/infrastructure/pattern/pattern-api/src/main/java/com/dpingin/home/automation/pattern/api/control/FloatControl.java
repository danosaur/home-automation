package com.dpingin.home.automation.pattern.api.control;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class FloatControl extends AbstractControl<Float>
{
	public FloatControl()
	{
		minimumValue = 0f;
		maximumValue = 1f;
		defaultValue = 0f;
	}

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

	@Override
	public void setValueString(String value)
	{
		setValue(Float.parseFloat(value));
	}
}
