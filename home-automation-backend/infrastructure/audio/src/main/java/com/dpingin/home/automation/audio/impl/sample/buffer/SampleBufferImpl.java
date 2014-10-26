package com.dpingin.home.automation.audio.impl.sample.buffer;

import com.dpingin.home.automation.audio.api.sample.buffer.SampleBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import uk.co.boundedbuffer.ConcurrentBlockingFloatQueue;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 03:27
 * To change this template use File | Settings | File Templates.
 */
public class SampleBufferImpl implements SampleBuffer
{
    private static Logger log = LoggerFactory.getLogger(SampleBufferImpl.class);
    protected int size;
    protected float[] buffer;

    protected Object lock = new Object();

    protected int fillCounter = 0;

    @Override
    public synchronized void addSamples(float[] samples)
    {
        int sampleCount = samples.length;
        Assert.isTrue(size >= sampleCount);

        if (buffer == null)
        {
            buffer = new float[size];
            log.debug("Initialized sample buffer with size {}", size);
        }

        int memorySize = size - sampleCount;

        float[] tempBuffer = new float[size];

        System.arraycopy(buffer, sampleCount, tempBuffer, 0, memorySize);
        System.arraycopy(samples, 0, tempBuffer, memorySize, sampleCount);

        fillCounter += sampleCount;
        if (fillCounter >= size)
        {
            fillCounter = size;
            synchronized (lock)
            {
                buffer = tempBuffer;
            }
        }
    }

    @Override
    public float[] getSamples()
    {
        while (buffer == null)
            try
            {
                Thread.sleep(20);
            } catch (InterruptedException e)
            {
            }

        synchronized (lock)
        {
            return Arrays.copyOf(buffer, buffer.length);
        }
    }

    @Override
    public synchronized void reset()
    {
        fillCounter = 0;
        buffer = null;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public SampleBufferImpl size(final int size)
    {
        this.size = size;
        return this;
    }


}
