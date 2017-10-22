package com.dpingin.home.automation.console.input;

import com.dpingin.home.automation.console.Application;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import com.dpingin.home.automation.rgb.controller.util.HexUtils;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Random;

@Component
public class InputHandler
{
	private static final Logger log = LoggerFactory.getLogger(InputHandler.class);

	private RgbController rgbController;

	@Autowired
	public InputHandler(RgbController rgbController)
	{
		this.rgbController = rgbController;
	}

	public boolean handleInput(String[] input) throws RgbControllerException
	{
		Option portOption = Option.builder("port").hasArg().desc("COM port name").build();

		Option rndOption = Option.builder("random").desc("Random color").build();

		Option colorOption = Option.builder("color").hasArg().desc("Selected color as #rrggbb").build();

		Option colorsOption = Option.builder("colors").numberOfArgs(3).desc("Selected color as 0xRR 0xGG 0xBB or RRR GGG BBB").build();

		Option flowOption = Option.builder("flow").numberOfArgs(3).optionalArg(true).desc("Smooth flow of colors").build();

		Option flashOption = Option.builder("flash").desc("Flash several times").build();

		Option strobeOption = Option.builder("strobe").desc("Strobe until stopped").build();

		Option stopOption = Option.builder("stop").desc("Stop strobe").build();

		Option offOption = Option.builder("off").desc("Turn off").build();

		Options options = new Options();
		options.addOption(portOption);
		options.addOption(rndOption);
		options.addOption(colorOption);
		options.addOption(colorsOption);
		options.addOption(flowOption);
		options.addOption(flashOption);
		options.addOption(strobeOption);
		options.addOption(stopOption);
		options.addOption(offOption);

		try
		{
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(options, input);


			if (commandLine.hasOption(portOption.getOpt()))
			{
				String port = commandLine.getOptionValue(portOption.getOpt());

				rgbController.setPortName(port);
				rgbController.init();
			}
			if (commandLine.hasOption(rndOption.getOpt()))
			{
				Random rnd = new Random(System.currentTimeMillis());
				int r = rnd.nextInt(256);
				int g = rnd.nextInt(256);
				int b = rnd.nextInt(256);

				return setColor(r, g, b);
			}
			else if (commandLine.hasOption(colorOption.getOpt()))
			{
				String color = commandLine.getOptionValue(colorOption.getOpt());

				if (color.startsWith("#") && color.length() == 7)
				{
					String[] colors = new String[]{color.substring(1, 3), color.substring(3, 5), color.substring(5, 7)};
					return setColor(colors, 16);
				}
				else
				{
					throw new ParseException("Color in format #rrggbb expected");
				}
			}
			else if (commandLine.hasOption(colorsOption.getOpt()))
			{
				String[] colors = commandLine.getOptionValues(colorsOption.getOpt());

				if (colors != null && colors.length == 3)
				{
					if (colors[0].startsWith("0x") && colors[1].startsWith("0x") && colors[2].startsWith("0x"))
					{
						colors[0] = colors[0].replace("0x", "");
						colors[1] = colors[1].replace("0x", "");
						colors[2] = colors[2].replace("0x", "");

						return setColor(colors, 16);
					}
					else
					{
						return setColor(colors, 10);
					}
				}
				else
				{
					throw new ParseException("Color in format 0xRR 0xGG 0xBB or RRR GGG BBB expected");
				}
			}
			else if (commandLine.hasOption(flowOption.getOpt()))
			{
				String[] values = commandLine.getOptionValues(flowOption.getOpt());

				//Value
				float v = 1f;
				if (values != null && values.length >= 1 && StringUtils.hasLength(values[0]))
				{
					v = Float.parseFloat(values[0]);
				}

				//Hue increment
				float increment = 0.01f;
				if (values != null && values.length >= 2 && StringUtils.hasLength(values[1]))
				{
					increment = Float.parseFloat(values[1]);
				}

				//Sleep time
				int sleepMillis = 100;
				if (values != null && values.length >= 3 && StringUtils.hasLength(values[2]))
				{
					sleepMillis = Integer.parseInt(values[2]);
				}

				//Saturation
				float s = 1f;
				if (values != null && values.length >= 4 && StringUtils.hasLength(values[3]))
				{
					s = Float.parseFloat(values[3]);
				}

				for (float h = 0; ; h += increment)
				{
					setColor(Color.fromHSB(h, s, v));
					sleep(sleepMillis);
					try
					{
						if (System.in.available() > 0)
						{
							return true;
						}
					}
					catch (IOException ignored)
					{
					}
					if (h == 1)
					{
						h = 0;
					}
				}
			}
			else if (commandLine.hasOption(flashOption.getOpt()))
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
			}
			else if (commandLine.hasOption(offOption.getOpt()))
			{
				return setColor(Color.BLACK);
			}
			else
			{
				throw new ParseException("Unknown command");
			}
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("start-console-app.bat [OPTIONS]", options);
		}
		return false;
	}

	private boolean setColor(String[] colors, int radix)
	{
		try
		{
			int r = Integer.parseInt(colors[0], radix);
			int g = Integer.parseInt(colors[1], radix);
			int b = Integer.parseInt(colors[2], radix);

			return setColor(r, g, b);
		}
		catch (NumberFormatException e)
		{
			log.error("Invalid number format: " + e.getMessage());
		}
		return false;
	}

	private boolean setColor(Color color)
	{
		return setColor(color.getRed(), color.getGreen(), color.getBlue());
	}

	private boolean setColor(int r, int g, int b)
	{
		try
		{
			System.out.println("Setting color: " + HexUtils.toHexString((byte) r) + " " + HexUtils.toHexString((byte) g) + " " + HexUtils.toHexString((byte) b));

			rgbController.setColor(r, g, b);

			return true;
		}
		catch (RgbControllerException e)
		{
			log.error("Error setting color: " + e.getMessage());
		}
		return false;
	}

	private void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException ignored)
		{
		}
	}
}
