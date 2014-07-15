package com.dpingin.home.automation.rgb.controller.api.color;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:17
 * To change this template use File | Settings | File Templates.
 */
public class Color
{
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color BLUE = new Color(0, 0, 255);

    protected int r;
    protected int g;
    protected int b;

    public Color()
    {
    }

    public Color(int r, int g, int b)
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

    public int getR()
    {
        return r;
    }

    public void setR(int r)
    {
        this.r = r;
    }

    public int getG()
    {
        return g;
    }

    public void setG(int g)
    {
        this.g = g;
    }

    public int getB()
    {
        return b;
    }

    public void setB(int b)
    {
        this.b = b;
    }
}
