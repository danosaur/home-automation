package com.dpingin.home.automation.audio.impl.audio.input.provider;

import com.dpingin.home.automation.audio.api.audio.input.provider.AbstractAudioInputProvider;
import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import com.dpingin.home.automation.audio.impl.audio.input.MinimAudioInputImpl;
import ddf.minim.Minim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 16.10.14
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
public class MinimAudioInputProviderImpl extends AbstractAudioInputProvider
{
    private final static Logger log = LoggerFactory.getLogger(MinimAudioInputProviderImpl.class);

    protected int audioInputBufferSize = 1024;

    protected Minim minim;

    public void init()
    {
        if (minim == null)
            minim = new Minim(this);

        audioInput = new MinimAudioInputImpl(minim.getLineIn(Minim.MONO, audioInputBufferSize));
    }

    public void destroy()
    {
        if (audioInput != null)
            audioInput.destroy();

        if (minim != null)
            minim.stop();
        minim = null;

    }

    @Override
    public AudioInput getAudioInput()
    {
        return audioInput;
    }

    public void setAudioInputBufferSize(int audioInputBufferSize)
    {
        this.audioInputBufferSize = audioInputBufferSize;
    }

    public MinimAudioInputProviderImpl audioInputBufferSize(final int audioInputBufferSize)
    {
        this.audioInputBufferSize = audioInputBufferSize;
        return this;
    }
}
