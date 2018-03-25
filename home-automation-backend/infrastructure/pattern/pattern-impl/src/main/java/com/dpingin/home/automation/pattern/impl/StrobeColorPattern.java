package com.dpingin.home.automation.pattern.impl;

import com.dpingin.home.automation.pattern.api.AbstractPattern;
import com.dpingin.home.automation.pattern.api.Pattern;
import com.dpingin.home.automation.pattern.api.PatternException;
import com.dpingin.home.automation.pattern.api.control.Controllable;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 01:29
 * To change this template use File | Settings | File Templates.
 */
public class StrobeColorPattern extends AbstractPattern implements Pattern
{
	private static final int DEFAULT_ON_TIME = 100; //ms

	private static final int DEFAULT_OFF_TIME = 900; //ms

	private final static Logger log = LoggerFactory.getLogger(StrobeColorPattern.class);

	@Controllable
	protected Color color = Color.WHITE;

	@Controllable
	protected int onTime = DEFAULT_ON_TIME;

	@Controllable
	protected int offTime = DEFAULT_OFF_TIME;

	protected ScheduledFuture scheduledFuture;

	protected Action action;

	protected ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	@Override
	@PostConstruct
	public void init()
	{
		super.init();
	}

	@Override
	public void start() throws PatternException
	{
		super.start();

		action = new Action();
		scheduledFuture = scheduledExecutorService.schedule(action, 0, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop()
	{
		super.stop();
		if (scheduledFuture != null)
			scheduledFuture.cancel(true);
		try
		{
			rgbController.setColor(Color.BLACK);
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}

		scheduledExecutorService.shutdownNow();
	}

	@Override
	protected void generatePattern()
	{
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

	public int getOffTime()
	{
		return offTime;
	}

	public void setOffTime(int offTime)
	{
		this.offTime = offTime;
	}

	public StrobeColorPattern frequency(final int frequency)
	{
		this.offTime = frequency;
		return this;
	}

	protected class Action implements Runnable
	{
		protected boolean on = false;

		protected long lastTime;

		@Override
		public void run()
		{
			try
			{
				if (on)
					rgbController.setColor(Color.BLACK);
				else
					rgbController.setColor(color);
			}
			catch (RgbControllerException e)
			{
				log.error("Failed to set color: " + color);
			}
			on = !on;

			if (running)
				scheduledFuture = scheduledExecutorService.schedule(action, on ? onTime : offTime, TimeUnit.MILLISECONDS);
		}
	}
}
