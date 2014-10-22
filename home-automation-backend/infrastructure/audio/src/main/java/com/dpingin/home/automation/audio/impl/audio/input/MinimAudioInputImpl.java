package com.dpingin.home.automation.audio.impl.audio.input;

import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import ddf.minim.AudioListener;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class MinimAudioInputImpl implements AudioInput
{
    ddf.minim.AudioInput audioInput;

    public MinimAudioInputImpl()
    {
    }

    public MinimAudioInputImpl(ddf.minim.AudioInput audioInput)
    {
        this.audioInput = audioInput;
    }

    @Override
    public float getSampleRate()
    {
        return audioInput.sampleRate();
    }

    @Override
    public int getBufferSize()
    {
        return audioInput.bufferSize();
    }

    @Override
    public void addListener(AudioListener audioListener)
    {
        audioInput.addListener(audioListener);
    }

    @Override
    public void removeListener(AudioListener audioListener)
    {
        audioInput.removeListener(audioListener);
    }

    @Override
    public void init()
    {
    }

    @Override
    public void destroy()
    {
        this.audioInput.close();
    }

    public void setAudioInput(ddf.minim.AudioInput audioInput)
    {
        this.audioInput = audioInput;
    }

    public MinimAudioInputImpl audioInput(final ddf.minim.AudioInput audioInput)
    {
        this.audioInput = audioInput;
        return this;
    }


}
