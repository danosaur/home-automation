package com.dpingin.home.automation.audio.api.pattern;

import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 23.07.14
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class SampleProcessorAwarePattern<T extends SampleProcessor> extends AbstractPattern implements Pattern
{
    private final static Logger log = LoggerFactory.getLogger(SampleProcessorAwarePattern.class);

    protected T sampleProcessor;

    public void setSampleProcessor(T sampleProcessor)
    {
        this.sampleProcessor = sampleProcessor;
    }

    public SampleProcessorAwarePattern sampleProcessor(final T sampleProcessor)
    {
        this.sampleProcessor = sampleProcessor;
        return this;
    }
}
