package com.dpingin.home.automation.audio.impl.audio.input;

import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import com.dpingin.home.automation.audio.api.audio.input.AudioInputException;
import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProviderException;
import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverListener;
import com.synthbot.jasiohost.AsioDriverState;
import ddf.minim.AudioListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 23:33
 * To change this template use File | Settings | File Templates.
 */
public class AsioAudioInputImpl implements AudioInput
{
    private static final Logger log = LoggerFactory.getLogger(AsioAudioInputImpl.class);
    protected int bufferSize = 1024;
    protected AsioDriver asioDriver;
    protected int channelIndex;
    protected AsioDriverListener asioDriverListener;
    protected Set<AudioListener> audioListeners = new HashSet<>();

    @Override
    public float getSampleRate()
    {
        return (float) asioDriver.getSampleRate();
    }

    @Override
    public int getBufferSize()
    {
        return bufferSize;
    }

    @Override
    public void addListener(AudioListener audioListener)
    {
        audioListeners.add(audioListener);
    }

    @Override
    public void removeListener(AudioListener audioListener)
    {
        audioListeners.remove(audioListener);
    }

    @Override
    public void init()
    {
        asioDriverListener = new AsioDriverListener()
        {
            @Override
            public void bufferSwitch(long systemTime, long samplePosition, Set<AsioChannel> channels)
            {
                log.trace("switch!");

                AsioChannel[] audioChannels = channels.toArray(new AsioChannel[0]);

                float[] samples = new float[audioChannels[0].getByteBuffer().limit() / 4];
                audioChannels[0].read(samples);

                for (AudioListener audioListener : audioListeners)
                {
                    try
                    {
                        audioListener.samples(samples);
                    } catch (Throwable e)
                    {
                        log.warn("Audio listener exception", e);
                    }
                }
            }

            @Override
            public void sampleRateDidChange(double sampleRate)
            {
            }

            @Override
            public void resetRequest()
            {
            }

            @Override
            public void resyncRequest()
            {
            }

            @Override
            public void bufferSizeChanged(int bufferSize)
            {
            }

            @Override
            public void latenciesChanged(int inputLatency, int outputLatency)
            {
            }
        };

        asioDriver.addAsioDriverListener(asioDriverListener);
    }

    @Override
    public void destroy()
    {
        audioListeners.clear();

        if (asioDriver != null && asioDriverListener != null)
        {
            asioDriver.removeAsioDriverListener(asioDriverListener);
            asioDriverListener = null;
        }
    }

    @Override
    public void start() throws AudioInputException
    {
        if (asioDriver.getCurrentState() == AsioDriverState.RUNNING)
            throw new AudioInputException("Audio channel is currently running");

        Set<AsioChannel> activeChannels = new HashSet<AsioChannel>();
        AsioChannel asioChannel = asioDriver.getChannelInput(channelIndex);
        activeChannels.add(asioChannel);

        log.info(String.format("Starting audio channel: %s", asioChannel.getChannelName()));

        asioDriver.createBuffers(activeChannels);

        asioDriver.start();
    }

    @Override
    public void stop()
    {
        if (asioDriver.getCurrentState() == AsioDriverState.RUNNING)
        {
            asioDriver.stop();
            asioDriver.disposeBuffers();
        }
    }

    public void setAsioDriver(AsioDriver asioDriver)
    {
        this.asioDriver = asioDriver;
    }

    public AsioAudioInputImpl asioDriver(final AsioDriver asioDriver)
    {
        this.asioDriver = asioDriver;
        return this;
    }

    public int getChannelIndex()
    {
        return channelIndex;
    }

    public void setChannelIndex(int channelIndex)
    {
        this.channelIndex = channelIndex;
    }

    public AsioAudioInputImpl channelIndex(final int channelIndex)
    {
        this.channelIndex = channelIndex;
        return this;
    }
}
