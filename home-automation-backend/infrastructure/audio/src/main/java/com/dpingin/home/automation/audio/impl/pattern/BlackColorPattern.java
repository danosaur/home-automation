package com.dpingin.home.automation.audio.impl.pattern;

import com.dpingin.home.automation.audio.api.pattern.AbstractPattern;
import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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

    protected RgbController rgbController;

    @Override
    public void init()
    {
        Assert.notNull(rgbController);
    }

    @Override
    public void stop()
    {
        super.stop();
        try
        {
            rgbController.setColor(Color.BLACK);
        } catch (RgbControllerException e)
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
        } catch (RgbControllerException e)
        {
            log.error("Failed to set color", e);
        }

        while (running)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
            }
        }
    }

    public void setRgbController(RgbController rgbController)
    {
        this.rgbController = rgbController;
    }

    public BlackColorPattern rgbController(final RgbController rgbController)
    {
        this.rgbController = rgbController;
        return this;
    }

}
