package com.dpingin.homeautomation.types;

public class ControlValueResponse
{
	private String patternName;
	private String controlName;
	private Object value;

	public ControlValueResponse(String patternName, String controlName, Object value)
	{
		this.patternName = patternName;
		this.controlName = controlName;
		this.value = value;
	}

	public String getPatternName()
	{
		return patternName;
	}

	public void setPatternName(String patternName)
	{
		this.patternName = patternName;
	}

	public String getControlName()
	{
		return controlName;
	}

	public void setControlName(String controlName)
	{
		this.controlName = controlName;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
}
