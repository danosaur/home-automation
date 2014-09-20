package com.dpingin.home.automation.audio.api.capture.test;

import com.dpingin.home.automation.audio.impl.tools.WindowFunction;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import ddf.minim.AudioInput;
import ddf.minim.AudioListener;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import org.jtransforms.fft.FloatFFT_1D;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sound.sampled.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/StreamingInputTest-context.xml")
public class StreamingInputTest
{
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RgbController rgbController;

    protected Mode mode = Mode.RGB;

    protected int bufferSize = 1024;

    protected int windowSize = 32;
    protected boolean useHalfWindow = true;

    protected float minBrightness = 0.0f;
    protected float maxBrightness = 1.0f;

    protected boolean dynamicValue = false;

    protected float gain = 0.4f;

    protected boolean applyLMHScale = false;

    protected float lowsGain = 0.012f;
    protected float midsGain = 0.5f;
    protected float highsGain = 2.5f;

    float maxLows = 0f;
    float maxMids = 0f;
    float maxHighs = 0f;

    protected long runTimeSeconds = 60 * 60 * 60;

    protected float lowsFrequencyFrom = 0f;
    protected float lowsFrequencyTo = 200f;
    protected float midsFrequencyFrom = 500f;
    protected float midsFrequencyTo = 8000f;
    protected float highsFrequencyFrom = 10000f;
    protected float highsFrequencyTo = 20000f;

    @Test
    public void minimTest() throws RgbControllerException
    {
        Minim minim = new Minim(this);
        minim.debugOn();
        final AudioInput lineIn = minim.getLineIn(Minim.MONO, bufferSize);

        log.info("Buffer size {}", lineIn.bufferSize());

        final FFT fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
        fft.window(FFT.HAMMING);

        WindowFunction windowFunction = new WindowFunction();
        windowFunction.setWindowType(WindowFunction.BLACKMAN);
        final double[] window = windowFunction.generate(useHalfWindow ? windowSize * 2 : windowSize);

//        for (int i = 0; i < 5; i++)
//        {
//            windowFunction.setWindowType(i);
//            double[] testWindow = windowFunction.generate(windowSize);
//            log.debug("Window[{}] {}", i, testWindow);
//        }

        lineIn.addListener(new AudioListener()
        {
            List<Color> prevColors = new LinkedList();

            @Override
            public void samples(float[] samp)
            {
                try
                {
                    processBuffer(samp);
                } catch (RgbControllerException e)
                {
                    log.error(e.getMessage(), e);
                }
            }

            @Override
            public void samples(float[] sampL, float[] sampR)
            {
            }

            private void processBuffer(float[] samples) throws RgbControllerException
            {
//                log.debug("Sample count: {}", samples.length);
//                log.trace("Samples: {}", samples);

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

                log.debug("LMH Raw: {} {} {}", rawAvgLows, rawAvgMids, rawAvgHighs);
                log.debug("LMH Max: {} {} {}", maxLows, maxMids, maxHighs);
                log.debug("LMH: {} {} {}", lows, mids, highs);
                log.debug("RGB: {} {} {}", color.getRed(), color.getGreen(), color.getBlue());
                log.debug("HSV: {} {} {}", color.getHue(), color.getSaturation(), color.getValue());
                log.debug("Brightness: {}", color.getBrightness());
                log.debug("Window: {}", window);

                if (prevColors.size() < windowSize)
                    prevColors.add(color);
                else
                {
                    if (prevColors.size() > 0)
                        prevColors.remove(0);
                    prevColors.add(color);
                    float rSum = 0;
                    float gSum = 0;
                    float bSum = 0;
                    for (int i = 0; i < windowSize; i++)
                    {
                        Color prevColor = prevColors.get(i);
                        rSum += window[i] * prevColor.getRed();
                        gSum += window[i] * prevColor.getGreen();
                        bSum += window[i] * prevColor.getBlue();
                    }
                    color = new Color(Math.round(rSum / prevColors.size()), Math.round(gSum / prevColors.size()), Math.round(bSum / prevColors.size()));
                    rgbController.setColor(color);
                }
            }
        });

        long startTime = System.currentTimeMillis();
        while (true)
        {
            if (System.currentTimeMillis() - startTime > runTimeSeconds * 100)
                break;

            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
            }
        }

        rgbController.setColor(Color.BLACK);
        lineIn.close();
        minim.stop();
    }

    public enum Mode
    {
        RGB,
        HSB
    }
}
