package com.dpingin.home.automation.pattern.impl;

import com.dpingin.home.automation.pattern.api.AbstractPattern;
import com.dpingin.home.automation.pattern.api.Pattern;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 01:29
 * To change this template use File | Settings | File Templates.
 */
public class BlackColorPattern extends AbstractPattern implements Pattern
{
	private final static Logger log = LoggerFactory.getLogger(BlackColorPattern.class);

	@Override
	@PostConstruct
	public void init()
	{
		super.init();
	}

	@Override
	public void stop()
	{
		super.stop();
		try
		{
			rgbController.setColor(Color.BLACK);
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}
	}

	@Override
	protected void generatePattern()
	{
		try
		{
			rgbController.setColor(Color.BLACK);
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}

		while (running)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}
