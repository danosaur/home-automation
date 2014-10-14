package com.dpingin.home.automation.audio.impl.pattern;

import com.dpingin.home.automation.audio.api.pattern.AbstractPattern;
import com.dpingin.home.automation.audio.api.pattern.Controllable;
import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.*;

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
    public void init()
    {
        Assert.notNull(rgbController);
    }

    @Override
    public void start()
    {
        super.start();    //To change body of overridden methods use File | Settings | File Templates.

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
        } catch (RgbControllerException e)
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
            } catch (InterruptedException e)
            {
            }
        }
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public StrobeColorPattern color(final Color color)
    {
        this.color = color;
        return this;
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
            } catch (RgbControllerException e)
            {
                log.error("Failed to set color: " + color);
            }
            on = !on;

            if (running)
                scheduledFuture = scheduledExecutorService.schedule(action, on ? onTime : offTime, TimeUnit.MILLISECONDS);
        }
    }
}
