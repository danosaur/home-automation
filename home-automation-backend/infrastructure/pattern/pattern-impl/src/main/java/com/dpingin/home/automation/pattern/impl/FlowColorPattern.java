package com.dpingin.home.automation.pattern.impl;

import com.dpingin.home.automation.pattern.api.AbstractPattern;
import com.dpingin.home.automation.pattern.api.Pattern;
import com.dpingin.home.automation.pattern.api.PatternException;
import com.dpingin.home.automation.pattern.api.control.Controllable;
import com.dpingin.home.automation.pattern.api.control.FloatControl;
import com.dpingin.home.automation.pattern.api.control.IntegerControl;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 01:29
 * To change this template use File | Settings | File Templates.
 */
public class FlowColorPattern extends AbstractPattern implements Pattern
{
	private final static Logger log = LoggerFactory.getLogger(FlowColorPattern.class);

	private long startTimestampNanos;

	@Controllable
	private FloatControl saturationControl;

	@Controllable
	private FloatControl valueControl;

	@Controllable
	private IntegerControl hueFullCycleTimeMillisControl;

	public FlowColorPattern()
	{
		this.saturationControl = new FloatControl()
			.name("saturation")
			.minimumValue(0f)
			.maximumValue(1f)
			.defaultValue(1f);

		this.valueControl = new FloatControl()
			.name("value")
			.minimumValue(0f)
			.maximumValue(1f)
			.defaultValue(1f);

		this.hueFullCycleTimeMillisControl = new IntegerControl()
			.name("hueFullCycleTime")
			.minimumValue(100)
			.maximumValue((int)TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
			.defaultValue((int)TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES));

		controls.add(saturationControl, valueControl, hueFullCycleTimeMillisControl);
	}

	@Override
	@PostConstruct
	public void init()
	{
		super.init();
	}

	@Override
	public void start() throws PatternException
	{
		startTimestampNanos = System.nanoTime();

		super.start();
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
		long elapsedTimeMillis = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTimestampNanos, TimeUnit.NANOSECONDS);

		float hue = (float)(elapsedTimeMillis % hueFullCycleTimeMillisControl.getValue()) / hueFullCycleTimeMillisControl.getValue();

		try
		{
			Color color = Color.fromHSB(hue, saturationControl.getValue(), valueControl.getValue());
			if (!color.equals(rgbController.getColor()))
				rgbController.setColor(color);
			else
				Thread.sleep(10);
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}
		catch (InterruptedException ignored)
		{
		}
	}
}
