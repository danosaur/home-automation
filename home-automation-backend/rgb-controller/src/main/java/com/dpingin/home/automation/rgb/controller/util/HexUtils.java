package com.dpingin.home.automation.rgb.controller.util;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 10.06.14
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class HexUtils
{
    public static final byte[] fromHexString(final String s)
    {
        String[] v = s.split(" ");
        byte[] arr = new byte[v.length];
        int i = 0;
        for (String val : v)
        {
            arr[i++] = Integer.decode("0x" + val).byteValue();

        }
        return arr;
    }

    public static String toHexString(byte[] data)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : data)
            stringBuilder.append(toHexString(b)).append(" ");
        return stringBuilder.toString();
    }

    public static String toHexString(byte data)
    {
        return String.format("0x%02X", data);
    }
}
