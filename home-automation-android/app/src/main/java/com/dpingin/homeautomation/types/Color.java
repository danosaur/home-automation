package com.dpingin.homeautomation.types;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.util.Assert;

/**
 * Created by Denis on 01.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Color
{
	protected int alpha = 255;
	protected int color;

	public Color()
	{
	}

	public Color(int color)
	{
		this.color = color;
		alpha = android.graphics.Color.alpha(color);
	}

	public Color(int red, int green, int blue)
	{
		color = android.graphics.Color.argb(alpha, red, green, blue);
	}

	public Color(float red, float green, float blue)
	{
		color = android.graphics.Color.argb(alpha, Math.round(red), Math.round(green), Math.round(blue));
	}

	public int getAlpha()
	{
		return alpha;
	}

	@JsonIgnore
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
	}

	public int getRed()
	{
		return android.graphics.Color.red(color);
	}

	public void setRed(int red)
	{
		Assert.isTrue(red >= 0 && red <= 255, "R must be in range [0..255]");
		color = android.graphics.Color.argb(alpha, red, android.graphics.Color.green(color), android.graphics.Color.blue(color));
	}

	public int getGreen()
	{
		return android.graphics.Color.green(color);
	}

	public void setGreen(int green)
	{
		Assert.isTrue(green >= 0 && green <= 255, "G must be in range [0..255]");
		color = android.graphics.Color.argb(alpha, android.graphics.Color.red(color), green, android.graphics.Color.blue(color));
	}

	public int getBlue()
	{
		return android.graphics.Color.blue(color);
	}

	public void setBlue(int blue)
	{
		Assert.isTrue(blue >= 0 && blue <= 255, "B must be in range [0..255]");
		color = android.graphics.Color.argb(alpha, android.graphics.Color.red(color), android.graphics.Color.green(color), blue);
	}

	@JsonIgnore
	public void setBrightness(float brightness)
	{
		float currentBrightness = getBrightness();
		float scaleFactor = brightness / currentBrightness;
		setRed((int) Math.min(getRed() * scaleFactor * scaleFactor, 255));
		setGreen((int) Math.min(getGreen() * scaleFactor * scaleFactor, 255));
		setBlue((int) Math.min(getBlue() * scaleFactor * scaleFactor, 255));
	}

	public float getBrightness()
	{
		return (float) Math.sqrt(.241f * getRed() / 255f + .691f * getGreen() / 255f + .068f * getBlue() / 255f);
	}

	public float getHue()
	{
		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		return hsv[0];
	}

	@JsonIgnore
	public void setHue(float hue)
	{
		float[] hsv = new float[3];
		hsv[0] = hue;
		hsv[1] = getSaturation();
		hsv[2] = getValue();
		color = android.graphics.Color.HSVToColor(hsv);
	}

	public float getSaturation()
	{
		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		return hsv[1];
	}

	@JsonIgnore
	public void setSaturation(float saturation)
	{
		float[] hsv = new float[3];
		hsv[0] = getHue();
		hsv[1] = saturation;
		hsv[2] = getValue();
		color = android.graphics.Color.HSVToColor(hsv);
	}

	public float getValue()
	{
		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		return hsv[2];
	}

	@JsonIgnore
	public void setValue(float value)
	{
		float[] hsv = new float[3];
		hsv[0] = getHue();
		hsv[1] = getSaturation();
		hsv[2] = value;
		color = android.graphics.Color.HSVToColor(hsv);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Color color1 = (Color) o;

		return color == color1.color;

	}

	@Override
	public int hashCode()
	{
		return color;
	}

	public int getColor()
	{
		return color;
	}
}
