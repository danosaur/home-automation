package com.dpingin.home.automation.pattern.impl;

import com.dpingin.home.automation.pattern.api.AbstractPattern;
import com.dpingin.home.automation.pattern.api.control.Controllable;
import com.dpingin.home.automation.pattern.api.control.IntegerControl;
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
public class StaticColorPattern extends AbstractPattern
{
	private final static Logger log = LoggerFactory.getLogger(StaticColorPattern.class);

	@Controllable
	private IntegerControl rControl;

	@Controllable
	private IntegerControl gControl;

	@Controllable
	private IntegerControl bControl;

	@Override
	@PostConstruct
	public void init()
	{
		super.init();

		rControl = new IntegerControl()
			.name("red")
			.maximumValue(255);

		gControl = new IntegerControl()
			.name("green")
			.maximumValue(255);

		bControl = new IntegerControl()
			.name("blue")
			.maximumValue(255);

		controls.add(rControl, gControl, bControl);
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
			Color color = getColorFromControls();
			if (!color.equals(rgbController.getColor()))
				rgbController.setColor(color);
			else
				Thread.sleep(10);
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}
		catch (InterruptedException e)
		{
		}
	}

	public Color getColorFromControls()
	{
		return new Color(rControl.getValue(), gControl.getValue(), bControl.getValue());
	}

	public void setColor(Color color)
	{
		rControl.setValue(color.getRed());
		gControl.setValue(color.getGreen());
		bControl.setValue(color.getBlue());
	}
}
