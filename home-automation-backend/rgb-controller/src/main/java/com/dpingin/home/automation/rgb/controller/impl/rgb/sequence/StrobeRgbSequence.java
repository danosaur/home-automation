package com.dpingin.home.automation.rgb.controller.impl.rgb.sequence;

import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.AbstractRgbSequence;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:16
 * To change this template use File | Settings | File Templates.
 */
public class StrobeRgbSequence extends AbstractRgbSequence
{
    protected TaskScheduler taskScheduler;
    protected ScheduledFuture scheduledFuture;

    protected float frequency = 1;
    protected Color color = new Color(255, 255, 255);

    public StrobeRgbSequence()
    {
        name = "strobe";
    }

    @Override
    public void start()
    {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(new ScheduledRunnable(), Math.round(500f / frequency));
    }

    @Override
    public void stop()
    {
        if (scheduledFuture == null)
            return;
        scheduledFuture.cancel(false);

        while (!scheduledFuture.isDone())
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
            }
        }
        scheduledFuture = null;

        try
        {
            rgbController.setColor(Color.BLACK);
        } catch (RgbControllerException e)
        {
        }
    }

    public void setFrequency(float frequency)
    {
        this.frequency = frequency;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setTaskScheduler(TaskScheduler taskScheduler)
    {
        this.taskScheduler = taskScheduler;
    }

    protected class ScheduledRunnable implements Runnable
    {
        boolean on = false;

        @Override
        public void run()
        {
            try
            {
                rgbController.setColor(on ? Color.BLACK : color);
                on = !on;
            } catch (RgbControllerException e)
            {
            }
        }
    }
}
