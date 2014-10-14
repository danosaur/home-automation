package com.dpingin.home.automation.audio.impl.pattern;

import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.audio.api.sample.buffer.SampleBuffer;
import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import com.dpingin.home.automation.audio.impl.sample.processor.output.ColorSampleProcessorOutput;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 01:29
 * To change this template use File | Settings | File Templates.
 */
public class MinimAudioInputColorPattern extends AudioInputColorPattern implements Pattern
{
    private final static Logger log = LoggerFactory.getLogger(MinimAudioInputColorPattern.class);

    protected int audioInputBufferSize = 1024;

    protected Minim minim;

    @Override
    public void init()
    {
        if (minim == null)
            minim = new Minim(this);

        audioInput = minim.getLineIn(Minim.MONO, audioInputBufferSize);

        super.init();
    }

    @Override
    public void start()
    {
        super.start();
    }

    @Override
    public void stop()
    {
        super.stop();
    }

    @Override
    public void destroy()
    {
        if (audioInput != null)
            audioInput.close();

        super.destroy();

        if (minim != null)
            minim.stop();
        minim = null;

    }

    @Override
    protected void generatePattern()
    {
        ColorSampleProcessorOutput colorSampleProcessorOutput = sampleProcessor.processSamples(sampleBuffer.getSamples(), audioInput.sampleRate());

        try
        {
            rgbController.setColor(colorSampleProcessorOutput.getValue());
        } catch (RgbControllerException e)
        {
            log.error("Failed to set color", e);
        }
    }

    public void setAudioInput(AudioInput audioInput)
    {
        this.audioInput = audioInput;
    }

    public MinimAudioInputColorPattern audioInput(final AudioInput audioInput)
    {
        this.audioInput = audioInput;
        return this;
    }

    public MinimAudioInputColorPattern sampleProcessor(final SampleProcessor<ColorSampleProcessorOutput> sampleProcessor)
    {
        this.sampleProcessor = sampleProcessor;
        return this;
    }

    public void setRgbController(RgbController rgbController)
    {
        this.rgbController = rgbController;
    }

    public MinimAudioInputColorPattern rgbController(final RgbController rgbController)
    {
        this.rgbController = rgbController;
        return this;
    }

    public void setSampleBuffer(SampleBuffer sampleBuffer)
    {
        this.sampleBuffer = sampleBuffer;
    }

    public MinimAudioInputColorPattern sampleBuffer(final SampleBuffer sampleBuffer)
    {
        this.sampleBuffer = sampleBuffer;
        return this;
    }


}
