package com.dpingin.home.automation.pattern.api.control;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class IntegerControl extends AbstractControl<Integer>
{

	public IntegerControl()
	{
		minimumValue = 0;
		maximumValue = 100;
		defaultValue = 0;
	}

	public IntegerControl name(final String name)
	{
		this.name = name;
		return this;
	}

	public IntegerControl value(final Integer value)
	{
		this.value = value;
		return this;
	}

	public IntegerControl minimumValue(final Integer minimumValue)
	{
		this.minimumValue = minimumValue;
		return this;
	}

	public IntegerControl maximumValue(final Integer maximumValue)
	{
		this.maximumValue = maximumValue;
		return this;
	}

	public IntegerControl defaultValue(final Integer defaultValue)
	{
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	public void setValueString(String value)
	{
		setValue(Integer.parseInt(value));
	}
}
