package com.dpingin.home.automation.rgb.controller.impl.cmd;

import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.player.RgbSequencePlayer;
import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.player.RgbSequencePlayerException;
import com.dpingin.home.automation.rgb.controller.util.HexUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 13.06.14
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public class RgbControllerCommandLine
{
    protected static RgbController rgbController;
    protected static RgbSequencePlayer rgbSequencePlayer;

    public static void main(String[] args) throws RgbControllerException
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
        rgbController = context.getBean(RgbController.class);
        rgbSequencePlayer = context.getBean(RgbSequencePlayer.class);

        if (args.length == 1)
            parseInput(args[0]);
        else if (args.length >= 3)
            parseInput(args[0] + " " + args[1] + " " + args[2]);

        System.out.println("Input format: RRR GGG BBB | 0xRR 0xGG | 0xBB | #rrggbb");

        while (true)
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try
            {
                String input = bufferedReader.readLine();

                if (!parseInput(input))
                    System.out.println("Input format: RRR GGG BBB | 0xRR 0xGG | 0xBB | #rrggbb");

            } catch (IOException e)
            {
                break;
            }
        }
    }

    private static boolean parseInput(String input) throws RgbControllerException
    {
        String[] split = input.split(" ");
//        if (split.length == 1)
//        {
            if (split[0].startsWith("#") && split[0].length() == 7)
            {
                String[] colors = new String[]{input.substring(1, 3), input.substring(3, 5), input.substring(5, 7)};

                return setColor(colors, 16);
            } else if ("rnd".equals(split[0]))
            {
                Random rnd = new Random(System.currentTimeMillis());
                int r = rnd.nextInt(256);
                int g = rnd.nextInt(256);
                int b = rnd.nextInt(256);

                return setColor(r, g, b);
            } else if ("flow".equals(split[0]))
            {
                float v = 1f;
                if (split.length >= 2 && StringUtils.hasLength(split[1]))
                    v = Float.parseFloat(split[1]);
                float increment = 0.01f;
                if (split.length >= 3 && StringUtils.hasLength(split[2]))
                    increment = Float.parseFloat(split[2]);
                int sleepMillis = 100;
                if (split.length >= 4 && StringUtils.hasLength(split[3]))
                    sleepMillis = Integer.parseInt(split[3]);
                float s = 1f;
                if (split.length >= 5 && StringUtils.hasLength(split[4]))
                    s = Float.parseFloat(split[4]);
                for (float h = 0; ; h += increment)
                {
                    setColor(Color.fromHSB(h, s, v));
                    sleep(sleepMillis);
                    try
                    {
                        if (System.in.available() > 0)
                            return true;
                    } catch (IOException e)
                    {
                    }
                    if (h == 1)
                        h = 0;
                }
            } else if ("flash".equals(split[0]))
            {
                for (int i = 0; i < 20; i++)
                {
                    Random rnd = new Random(System.currentTimeMillis());

                    int r = rnd.nextInt(256);
                    int g = rnd.nextInt(256);
                    int b = rnd.nextInt(256);

                    setColor(r, g, b);

                    sleep(100);

                    setColor(0, 0, 0);

                    sleep(100);
                }
                return true;
            } else if ("strobe".equals(split[0]))
            {
                try
                {
                    rgbSequencePlayer.play("strobe");
                    return true;
                } catch (RgbSequencePlayerException e)
                {
                    System.out.println("Failed to play sequence: " + e.getMessage());
                }
                return false;
            } else if ("stop".equals(split[0]))
            {
                rgbSequencePlayer.stop();
                return true;
            } else if ("off".equals(split[0]))
            {
                rgbController.setColor(Color.BLACK);
                return true;
            } else if ("colors".equals(split[0]))
            {
                for (int i = 0; i < 20; i++)
                {
                    setColor(255, 0, 0);

                    sleep(200);

                    setColor(0, 0, 255);

                    sleep(200);

                    setColor(255, 128, 0);

                    sleep(200);

                    setColor(0, 255, 0);

                    sleep(200);

                    setColor(0, 255, 255);

                    sleep(200);

                    setColor(255, 255, 0);

                    sleep(200);
                }
                return true;
            }
//        } else if (split.length == 3)
//        {
//            if (split[0].startsWith("0x") &&
//                    split[1].startsWith("0x") &&
//                    split[2].startsWith("0x"))
//            {
//                split[0] = split[0].replace("0x", "");
//                split[1] = split[1].replace("0x", "");
//                split[2] = split[2].replace("0x", "");
//
//                return setColor(split, 16);
//            } else
//            {
//                return setColor(split, 10);
//            }
//        }
        return false;
    }

    protected static boolean setColor(String[] colors, int radix)
    {
        try
        {
            int r = Integer.parseInt(colors[0], radix);
            int g = Integer.parseInt(colors[1], radix);
            int b = Integer.parseInt(colors[2], radix);

            return setColor(r, g, b);
        } catch (NumberFormatException e)
        {
            System.out.println("Wrong number format: " + e.getMessage());
        }
        return false;
    }

    protected static boolean setColor(Color color)
    {
        return setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    protected static boolean setColor(int r, int g, int b)
    {
        try
        {
            System.out.println("Setting color: " + HexUtils.toHexString((byte) r) + " " + HexUtils.toHexString((byte) g) + " " + HexUtils.toHexString((byte) b));

            rgbController.setColor(r, g, b);

            return true;
        } catch (RgbControllerException e)
        {
            System.out.println("Error setting color: " + e.getMessage());
        }
        return false;
    }

    protected static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
        }
    }
}
