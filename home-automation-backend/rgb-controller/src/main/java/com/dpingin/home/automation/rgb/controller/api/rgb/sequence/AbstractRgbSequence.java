package com.dpingin.home.automation.rgb.controller.api.rgb.sequence;

import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRgbSequence implements RgbSequence
{
    protected String name;
    protected RgbController rgbController;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public RgbController getRgbController()
    {
        return rgbController;
    }

    public void setRgbController(RgbController rgbController)
    {
        this.rgbController = rgbController;
    }
}
