package com.dpingin.home.automation.audio.impl.sample.processor;

import com.dpingin.home.automation.audio.api.sample.processor.AbstractSampleProcessor;
import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import com.dpingin.home.automation.audio.impl.sample.processor.output.ColorSampleProcessorOutput;
import com.dpingin.home.automation.audio.impl.window.function.WindowFunction;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import ddf.minim.analysis.FourierTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class BeatDetectorSampleProcessor extends AbstractSampleProcessor<ColorSampleProcessorOutput> implements SampleProcessor
{
    private final static Logger log = LoggerFactory.getLogger(BeatDetectorSampleProcessor.class);

    BeatDetect beatDetect;

    public void init()
    {
    }

    @Override
    public ColorSampleProcessorOutput processSamples(float[] samples, float sampleRate)
    {
        Assert.notNull(samples);

        if (beatDetect == null)
            beatDetect = new BeatDetect(samples.length, sampleRate);

        beatDetect.detect(samples);

        beatDetect.setSensitivity(200);

        Color color = Color.BLACK;
        if (beatDetect.isRange(3, 20, 5))
            color = Color.WHITE;

        return new ColorSampleProcessorOutput(color);
    }
}