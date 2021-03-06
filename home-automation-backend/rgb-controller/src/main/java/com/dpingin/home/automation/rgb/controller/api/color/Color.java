package com.dpingin.home.automation.rgb.controller.api.color;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:17
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Color
{
	public static final Color BLACK = new Color(0, 0, 0);

	public static final Color WHITE = new Color(255, 255, 255);

	public static final Color RED = new Color(255, 0, 0);

	public static final Color GREEN = new Color(0, 255, 0);

	public static final Color BLUE = new Color(0, 0, 255);

	protected float r;

	protected float g;

	protected float b;

	public Color()
	{
	}

	public Color(int r, int g, int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Color(float r, float g, float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public static Color fromHSB(float h, float s, float b)
	{
		java.awt.Color color = new java.awt.Color(java.awt.Color.HSBtoRGB(h, s, b));
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	public float getHue()
	{
		float[] values = java.awt.Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		return values[0];
	}

	@JsonIgnore
	public void setHue(float value)
	{
		java.awt.Color color = new java.awt.Color(java.awt.Color.HSBtoRGB(value, getSaturation(), getValue()));
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
	}

	public float getSaturation()
	{
		float[] values = java.awt.Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		return values[1];
	}

	@JsonIgnore
	public void setSaturation(float value)
	{
		java.awt.Color color = new java.awt.Color(java.awt.Color.HSBtoRGB(getHue(), value, getValue()));
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
	}

	public float getValue()
	{
		float[] values = java.awt.Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		return values[2];
	}

	@JsonIgnore
	public void setValue(float value)
	{
		java.awt.Color color = new java.awt.Color(java.awt.Color.HSBtoRGB(getHue(), getSaturation(), value));
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
	}

	public int getRed()
	{
		return Math.round(r);
	}

	public void setRed(int r)
	{
		Assert.isTrue(r >= 0 && r <= 255, "R must be in range [0..255]");
		this.r = r;
	}

	public int getGreen()
	{
		return Math.round(g);
	}

	public void setGreen(int g)
	{
		Assert.isTrue(g >= 0 && g <= 255, "G must be in range [0..255]");
		this.g = g;
	}

	public int getBlue()
	{
		return Math.round(b);
	}

	public void setBlue(int b)
	{
		Assert.isTrue(b >= 0 && b <= 255, "B must be in range [0..255]");
		this.b = b;
	}

	@JsonIgnore
	public void setBrightness(float brightness)
	{
		float currentBrightness = getBrightness();
		float scaleFactor = brightness / currentBrightness;
		r = r * scaleFactor * scaleFactor;
		g = g * scaleFactor * scaleFactor;
		b = b * scaleFactor * scaleFactor;
		limit();
	}

	protected void limit()
	{
		if (r > 255f)
			r = 255f;
		if (g > 255f)
			g = 255f;
		if (b > 255f)
			b = 255f;
	}

	public float getBrightness()
	{
		return (float)Math.sqrt(.241f * r / 255f + .691f * g / 255f + .068f * b / 255f);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof Color))
			return false;

		Color color = (Color)o;

		if (Float.compare(color.b, b) != 0)
			return false;
		if (Float.compare(color.g, g) != 0)
			return false;
		if (Float.compare(color.r, r) != 0)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
		result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
		result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
		return result;
	}
}
