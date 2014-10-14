package com.dpingin.home.automation.audio.impl.sample.processor;

import com.dpingin.home.automation.audio.api.sample.processor.AbstractSampleProcessor;
import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import com.dpingin.home.automation.audio.impl.sample.processor.output.ColorSampleProcessorOutput;
import com.dpingin.home.automation.audio.impl.window.function.WindowFunction;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
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
public class SpectrumColorSampleProcessor extends AbstractSampleProcessor<ColorSampleProcessorOutput> implements SampleProcessor
{
    private final static Logger log = LoggerFactory.getLogger(SpectrumColorSampleProcessor.class);
    protected FourierTransform fft;
    protected Mode mode = Mode.RGB;
    protected float minBrightness = 0.0f;
    protected float maxBrightness = 1.0f;
    protected boolean dynamicValue = false;
    protected float gain = 0.4f;
    protected boolean applyLMHScale = false;
    protected float lowsGain = 0.012f;
    protected float midsGain = 0.5f;
    protected float highsGain = 2.5f;
    protected float lowsFrequencyFrom = 0f;
    protected float lowsFrequencyTo = 200f;
    protected float midsFrequencyFrom = 500f;
    protected float midsFrequencyTo = 8000f;
    protected float highsFrequencyFrom = 10000f;
    protected float highsFrequencyTo = 20000f;
    protected int windowSize = 1;
    protected boolean useHalfWindow = true;
    float maxLows = 0f;
    float maxMids = 0f;
    float maxHighs = 0f;
    List<Color> prevColors = new LinkedList<>();
    double[] window;

    public void init()
    {
        window = new WindowFunction()
                .windowType(WindowFunction.BLACKMAN)
                .generate(useHalfWindow ? windowSize * 2 : windowSize);
    }

    @Override
    public ColorSampleProcessorOutput processSamples(float[] samples, float sampleRate)
    {
        Assert.notNull(samples);

        if (fft == null || fft.timeSize() != samples.length)
        {
            fft = new FFT(samples.length, sampleRate);
            fft.window(FFT.HAMMING);
        }

        log.trace("Sample count: {}", samples.length);
        log.trace("Samples: {}", samples);

        //Apply gain scale
        for (int i = 0; i < samples.length; i++)
            samples[i] = samples[i] * gain;

        fft.forward(samples);

        float rawAvgLows = fft.calcAvg(lowsFrequencyFrom, lowsFrequencyTo);
        float avgLows = rawAvgLows * lowsGain;

        float rawAvgMids = fft.calcAvg(midsFrequencyFrom, midsFrequencyTo);
        float avgMids = rawAvgMids * midsGain;

        float rawAvgHighs = fft.calcAvg(highsFrequencyFrom, highsFrequencyTo);
        float avgHighs = rawAvgHighs * highsGain;

        float lows = avgLows;
        float mids = avgMids;
        float highs = avgHighs;

        if (avgLows > maxLows)
            maxLows = avgLows;
        if (avgMids > maxMids)
            maxMids = avgMids;
        if (avgHighs > maxHighs)
            maxHighs = avgHighs;

        if (applyLMHScale)
        {
            lows = (maxLows - lows) / maxLows;
            mids = (maxMids - mids) / maxMids;
            highs = (maxHighs - highs) / maxHighs;
        }

        float lowsClipped = lows <= 1f ? lows : 1f;
        float midsClipped = mids <= 1f ? mids : 1f;
        float highsClipped = highs <= 1f ? highs : 1f;

        Color color;
        if (mode == Mode.HSB)
            color = Color.fromHSB(midsClipped, highsClipped, lowsClipped);
        else
            color = new Color(lowsClipped * 255f, midsClipped * 255f, highsClipped * 255f);

        if (color.getBrightness() < minBrightness)
            color.setBrightness(minBrightness);
        if (color.getBrightness() > maxBrightness)
            color.setBrightness(maxBrightness);

        if (dynamicValue)
        {
            float value = color.getValue();
            value *= lows;
            color.setValue(value);
        }

//        log.debug("LMH Raw: {} {} {}", rawAvgLows, rawAvgMids, rawAvgHighs);
//        log.debug("LMH Max: {} {} {}", maxLows, maxMids, maxHighs);
        log.debug("LMH: {} {} {}", lows, mids, highs);
        log.debug("RGB: {} {} {}", color.getRed(), color.getGreen(), color.getBlue());
        log.debug("HSV: {} {} {}", color.getHue(), color.getSaturation(), color.getValue());
        log.debug("Brightness: {}", color.getBrightness());

//        if (prevColors.size() < windowSize)
//        {
//            prevColors.add(color);
//            return new ColorSampleProcessorOutput(Color.BLACK);
//        } else
//        {
//            if (prevColors.size() > 0)
//                prevColors.remove(0);
//            prevColors.add(color);
//            float rSum = 0;
//            float gSum = 0;
//            float bSum = 0;
//            for (int i = 0; i < windowSize; i++)
//            {
//                Color prevColor = prevColors.get(i);
//                rSum += window[i] * prevColor.getRed();
//                gSum += window[i] * prevColor.getGreen();
//                bSum += window[i] * prevColor.getBlue();
//            }
//            color = new Color(Math.round(rSum / prevColors.size()), Math.round(gSum / prevColors.size()), Math.round(bSum / prevColors.size()));

            return new ColorSampleProcessorOutput(color);
//        }
    }

    public void setFft(FourierTransform fft)
    {
        this.fft = fft;
    }

    public SpectrumColorSampleProcessor fft(final FourierTransform fft)
    {
        this.fft = fft;
        return this;
    }

    public enum Mode
    {
        RGB,
        HSB
    }
}