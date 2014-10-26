package com.dpingin.home.automation.audio.api.audio.input;

import ddf.minim.AudioListener;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public interface AudioInput
{
    float getSampleRate();
    int getBufferSize();

    void addListener(AudioListener audioListener);
    void removeListener(AudioListener audioListener);

    void init();
    void destroy();

    void start() throws AudioInputException;
    void stop();
}
