package com.dpingin.home.automation.audio.api.sample.buffer;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 01:44
 * To change this template use File | Settings | File Templates.
 */
public interface SampleBuffer
{
    void addSamples(float[] samples);

    float[] getSamples();

    void reset();
}
