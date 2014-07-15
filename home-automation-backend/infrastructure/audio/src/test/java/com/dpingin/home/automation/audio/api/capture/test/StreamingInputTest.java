package com.dpingin.home.automation.audio.api.capture.test;

import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import ddf.minim.AudioInput;
import ddf.minim.AudioListener;
import ddf.minim.AudioSample;
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
    @Autowired
    RgbController rgbController;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void minimTest() throws RgbControllerException
    {
        Minim minim = new Minim(this);
        minim.debugOn();
        AudioInput lineIn = minim.getLineIn();

        AudioSample sample = minim.createSample(null, null);
        sample.addListener(new AudioListener()
        {
            @Override
            public void samples(float[] samp)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void samples(float[] sampL, float[] sampR)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        FFT fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());

        float maxLows = 0f;
        float maxMids = 0f;
        float maxHighs = 0f;
        while (true)
        {
            fft.forward(lineIn.mix);
            fft.window(FFT.HAMMING);

            float avgLows = fft.calcAvg(0f, 100f);
            float avgHighs = fft.calcAvg(5000f, 20000f);
            float avgMids = fft.calcAvg(500f, 4000f);

            if (avgLows > maxLows)
                maxLows = avgLows;
            if (avgMids > maxMids)
                maxMids = avgMids;
            if (avgHighs > maxHighs)
                maxHighs = avgHighs;

            float h = (maxMids - avgMids) * (float)Math.PI / maxMids;
            float s = (maxHighs - avgHighs) / maxHighs;
            float v = (maxLows - avgLows) / maxLows;
            Color color = Color.fromHSB(h, s, v);

            log.info("avgLows: " + avgLows + ", maxLows: " + maxLows + ", v: " + v);
            log.info("avgMids: " + avgMids + ", maxMids: " + maxMids + ", h: " + h);
            log.info("avgHighs: " + avgHighs + ", maxHighs: " + maxHighs + ", s: " + s);

            rgbController.setColor(color);

//            try
//            {
//                Thread.sleep(20);
//            } catch (InterruptedException e)
//            {
//            }
        }

//        lineIn.close();
//        minim.stop();
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
