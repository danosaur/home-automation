package com.dpingin.home.automation.pattern.api.control;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractControl<T extends Comparable<T>> implements Control<T>
{
	protected String name;

	protected T value;

	protected T minimumValue;

	protected T maximumValue;

	protected T defaultValue;

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void setValue(T value)
	{
		if (maximumValue != null && value.compareTo(maximumValue) > 0)
			this.value = maximumValue;
		else if (minimumValue != null && value.compareTo(minimumValue) < 0)
			this.value = minimumValue;
		else
			this.value = value;
	}

	@Override
	public T getValue()
	{
		if (value == null)
			value = defaultValue;
		return value;
	}

	public T getMinimumValue()
	{
		return minimumValue;
	}

	public void setMinimumValue(T minimumValue)
	{
		this.minimumValue = minimumValue;
	}

	public T getMaximumValue()
	{
		return maximumValue;
	}

	public void setMaximumValue(T maximumValue)
	{
		this.maximumValue = maximumValue;
	}

	public T getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(T defaultValue)
	{
		this.defaultValue = defaultValue;
	}
}
