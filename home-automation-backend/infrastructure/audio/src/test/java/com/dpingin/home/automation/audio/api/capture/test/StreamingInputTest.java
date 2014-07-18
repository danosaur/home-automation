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

    protected int bufferSize = 1024;

    protected int windowSize = 24;
    protected boolean useHalfWindow = true;

    protected float minBrightness = 0.3f;
    protected float maxBrightness = 0.3f;

    protected boolean dynamicValue = false;

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
            private float maxLows = 0f;
            private float maxMids = 0f;
            private float maxHighs = 0f;

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

                fft.forward(samples);

                float avgLows = fft.calcAvg(0f, 60f);
                float avgHighs = fft.calcAvg(10000f, 20000f);
                float avgMids = fft.calcAvg(500f, 8000f);

                if (avgLows > maxLows)
                    maxLows = avgLows;
                if (avgMids > maxMids)
                    maxMids = avgMids;
                if (avgHighs > maxHighs)
                    maxHighs = avgHighs;

                float lows = (maxLows - avgLows) / maxLows;
                float mids = (maxMids - avgMids) / maxMids;
                float highs = (maxHighs - avgHighs) / maxHighs;

//                Color color = Color.fromHSB(mids, highs, lows);
                Color color = new Color((1f - lows) * 255f, (1f - mids) * 255f, (1f - highs) * 255f);

                if (color.getBrightness() < minBrightness)
                    color.setBrightness(minBrightness);
                if (color.getBrightness() > maxBrightness)
                    color.setBrightness(minBrightness);

                if (dynamicValue)
                {
                    float value = color.getValue();
                    value *= lows;
                    color.setValue(value);
                }

                log.debug("MaxLMH: {} {} {}", maxLows, maxMids, maxHighs);
                log.debug("LMH: {} {} {}", lows, mids, highs);
                log.debug("RGB: {} {} {}", color.getRed(), color.getGreen(), color.getBlue());
                log.debug("HSV: {} {} {}", color.getHue(), color.getSaturation(), color.getValue());
                log.debug("Brightness: {}", color.getBrightness());
                log.debug("Window: {}", window);

                if (prevColors.size() < windowSize)
                {
                    prevColors.add(color);
                }
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
            if (System.currentTimeMillis() - startTime > 100000000)
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

    @Test
    @Ignore
    public void dataLineTest() throws LineUnavailableException, RgbControllerException
    {
        float sampleRate = 48000f;
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine dataLine;

        dataLine = (TargetDataLine) AudioSystem.getLine(info);

        try
        {
            dataLine.open(audioFormat);

            dataLine.addLineListener(new LineListener()
            {
                @Override
                public void update(LineEvent event)
                {
                    log.info("A new line event occurred: " + event.toString());
                }
            });

            dataLine.start();

            int sampleCount = 256;

            FloatFFT_1D fft = new FloatFFT_1D(sampleCount);

            int frameSize = dataLine.getFormat().getFrameSize();

            Assert.assertEquals(2, frameSize);

            int bufferSize = frameSize * sampleCount;
            byte[] buffer = new byte[bufferSize];
            while (true)
            {
                int bytesRead = dataLine.read(buffer, 0, bufferSize);

                float[] data = new float[sampleCount * 2];
                for (int i = 0; i < bytesRead; i += 2)
                {
                    short intSample = (short) (((buffer[i] & 0xFF) << 8) | (buffer[i + 1] & 0xFF));
//                    int intSample = (buffer[i]) | ((buffer[i + 1]) * 256);


                    data[i * 2 / frameSize] = (float) intSample / 65536 / 2;
                    data[i * 2 / frameSize + 1] = 0f;
                }

                fft.complexForward(data);

                float[] values = new float[data.length / 2];
                for (int i = 0; i < values.length; i++)
                {
                    float real = data[i];
                    float img = data[i + 1];

                    float value = (float) Math.sqrt(real * real + img * img);
                    values[i] = value;
                }

                float binWidth = (sampleRate / 2f) / (sampleCount / 2);
                float lpFrequency = 100f;
                float sum = 0f;
                int count = 0;
                for (int i = 0; i < values.length; i++)
                {
                    //LP
                    if (binWidth * i < lpFrequency)
                    {
                        sum += values[i];
                        count++;
                    }
                }
                float avg = sum / count;

                int r = (int) avg;
                if (r > 255)
                    r = 255;
                if (r < 0)
                    r = 0;

                log.info("Avg: " + avg);
                log.info("R: " + r);

                rgbController.setColor(r, 0, 0);
            }
        } finally
        {
            if (dataLine.isRunning())
                dataLine.stop();
            if (dataLine.isOpen())
                dataLine.close();
        }

    }
}
