package com.dpingin.home.automation.audio.impl.sample.processor.output;

import com.dpingin.home.automation.audio.api.sample.processor.output.AbstractSampleProcessorOutput;
import com.dpingin.home.automation.rgb.controller.api.color.Color;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 00:34
 * To change this template use File | Settings | File Templates.
 */
public class ColorSampleProcessorOutput extends AbstractSampleProcessorOutput
{
    protected Color color;

    public ColorSampleProcessorOutput()
    {
    }

    public ColorSampleProcessorOutput(Color value)
    {
        this.color = value;
    }

    @Override
    public Color getValue()
    {
        return color;
    }
}