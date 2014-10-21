package com.dpingin.home.automation.audio.api.pattern;

import com.dpingin.home.automation.audio.api.pattern.control.Control;
import com.dpingin.home.automation.audio.api.pattern.control.Controls;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 23.07.14
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPattern implements Pattern
{
    private final static Logger log = LoggerFactory.getLogger(AbstractPattern.class);

    protected Thread thread;
    protected boolean running = false;
    protected String name;

    protected RgbController rgbController;
    protected Controls controls;

    @Override
    public void init()
    {
    }

    @Override
    public void destroy()
    {
    }

    public boolean isRunning()
    {
        return running;
    }

    @Override
    public void start()
    {
        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                running = true;
                while (running)
                {
                    long startTime = System.nanoTime();

                    generatePattern();

                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;

                    log.debug("Pattern render duration: {} ms", duration / 1024 / 1024);
                }
            }
        });
        thread.start();
    }

    @Override
    public void stop()
    {
        running = false;
        thread.interrupt();
        try
        {
            thread.join();
        } catch (InterruptedException e)
        {
            log.error("Interrupted", e);
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Controls getControls()
    {
        return controls;
    }

    @Override
    public void setControls(Controls controls)
    {
        this.controls = controls;
    }

    @Override
    public void updateControls(Controls controls)
    {
        for (Control control : controls.values())
        {
            Control thisControl = this.controls.get(control.getName());
            if (thisControl != null)
                thisControl.setValue(control.getValue());
        }
    }

    @Override
    public void updateControls(Collection<? extends Control> controls)
    {
        for (Control control : controls)
        {
            Control thisControl = this.controls.get(control.getName());
            if (thisControl != null)
                thisControl.setValue(control.getValue());
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public AbstractPattern name(final String name)
    {
        this.name = name;
        return this;
    }

    public RgbController getRgbController()
    {
        return this.rgbController;
    }

    public void setRgbController(RgbController rgbController)
    {
        this.rgbController = rgbController;
    }

    public AbstractPattern rgbController(final RgbController rgbController)
    {
        this.rgbController = rgbController;
        return this;
    }

    protected abstract void generatePattern();
}
