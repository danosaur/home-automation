package com.dpingin.home.automation.rgb.controller.api.rgb.sequence.player;

import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.RgbSequence;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:36
 * To change this template use File | Settings | File Templates.
 */
public interface RgbSequencePlayer
{
    void addSequence(RgbSequence rgbSequence);
    void play(String name) throws RgbSequencePlayerException;
    void stop();
}
