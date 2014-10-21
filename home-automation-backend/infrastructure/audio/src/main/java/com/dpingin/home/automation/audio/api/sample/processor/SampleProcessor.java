package com.dpingin.home.automation.audio.api.sample.processor;

import com.dpingin.home.automation.audio.api.pattern.control.Controls;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 00:18
 * To change this template use File | Settings | File Templates.
 */
public interface SampleProcessor<T extends SampleProcessorOutput>
{
    void setControls(Controls controls);
    T processSamples(float[] samples, float sampleRate);
}
