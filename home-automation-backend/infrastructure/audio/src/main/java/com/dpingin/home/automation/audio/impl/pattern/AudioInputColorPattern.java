package com.dpingin.home.automation.audio.impl.pattern;

import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProvider;
import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.audio.api.pattern.SampleProcessorAwarePattern;
import com.dpingin.home.automation.audio.api.sample.buffer.SampleBuffer;
import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import com.dpingin.home.automation.audio.impl.sample.processor.output.ColorSampleProcessorOutput;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import ddf.minim.AudioListener;
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
public class AudioInputColorPattern extends SampleProcessorAwarePattern<SampleProcessor<ColorSampleProcessorOutput>> implements Pattern
{
    private final static Logger log = LoggerFactory.getLogger(AudioInputColorPattern.class);
    protected AudioInputProvider audioInputProvider;
    protected AudioInput audioInput;
    protected SampleBuffer sampleBuffer;
    protected RgbController rgbController;
    protected AudioListener audioListener;

    @Override
    public void init()
    {
        super.init();

        Assert.notNull(audioInputProvider);
        Assert.notNull(sampleBuffer);
        Assert.notNull(rgbController);

        audioInput = audioInputProvider.getAudioInput();
    }

    @Override
    public void start()
    {
        float audioInputBufferSize = audioInput.getBufferSize();
        log.debug("Audio input buffer size {}", audioInputBufferSize);

        float sampleRate = audioInput.getSampleRate();
        log.debug("Audio input sample rate {}", sampleRate);

        audioListener = new AudioListener()
        {
            @Override
            public void samples(float[] samples)
            {
                sampleBuffer.addSamples(samples);
            }

            @Override
            public void samples(float[] sampL, float[] sampR)
            {
            }
        };
        audioInput.addListener(audioListener);

        super.start();
    }

    @Override
    public void stop()
    {
        super.stop();

        audioInput.removeListener(audioListener);

        try
        {
            rgbController.setColor(Color.BLACK);
        } catch (RgbControllerException e)
        {
            log.error("Failed to set color", e);
        }
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }

    @Override
    protected void generatePattern()
    {
        ColorSampleProcessorOutput colorSampleProcessorOutput = sampleProcessor.processSamples(sampleBuffer.getSamples(), audioInput.getSampleRate());

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

    public AudioInputColorPattern audioInput(final AudioInput audioInput)
    {
        this.audioInput = audioInput;
        return this;
    }

    public AudioInputColorPattern sampleProcessor(final SampleProcessor<ColorSampleProcessorOutput> sampleProcessor)
    {
        this.sampleProcessor = sampleProcessor;
        return this;
    }

    public void setRgbController(RgbController rgbController)
    {
        this.rgbController = rgbController;
    }

    public AudioInputColorPattern rgbController(final RgbController rgbController)
    {
        this.rgbController = rgbController;
        return this;
    }

    public void setSampleBuffer(SampleBuffer sampleBuffer)
    {
        this.sampleBuffer = sampleBuffer;
    }

    public AudioInputColorPattern sampleBuffer(final SampleBuffer sampleBuffer)
    {
        this.sampleBuffer = sampleBuffer;
        return this;
    }

    public void setAudioInputProvider(AudioInputProvider audioInputProvider)
    {
        this.audioInputProvider = audioInputProvider;
    }

    public AudioInputColorPattern audioInputProvider(final AudioInputProvider audioInputProvider)
    {
        this.audioInputProvider = audioInputProvider;
        return this;
    }
}
