package com.dpingin.home.automation.audio.impl.minim.audio.input;

import com.dpingin.home.automation.audio.api.minim.audio.input.AudioInputProvider;
import ddf.minim.AudioInput;
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
public class AudioInputProviderImpl implements AudioInputProvider
{
    private final static Logger log = LoggerFactory.getLogger(AudioInputProviderImpl.class);

    protected int audioInputBufferSize = 1024;

    protected Minim minim;
    protected AudioInput audioInput;

    public void init()
    {
        if (minim == null)
            minim = new Minim(this);

        audioInput = minim.getLineIn(Minim.MONO, audioInputBufferSize);
    }

    public void destroy()
    {
        if (audioInput != null)
            audioInput.close();

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

    public AudioInputProviderImpl audioInputBufferSize(final int audioInputBufferSize)
    {
        this.audioInputBufferSize = audioInputBufferSize;
        return this;
    }
}
