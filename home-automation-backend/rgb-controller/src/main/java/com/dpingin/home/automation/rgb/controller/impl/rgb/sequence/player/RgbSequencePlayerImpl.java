package com.dpingin.home.automation.rgb.controller.impl.rgb.sequence.player;

import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.RgbSequence;
import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.player.RgbSequencePlayer;
import com.dpingin.home.automation.rgb.controller.api.rgb.sequence.player.RgbSequencePlayerException;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 14.06.14
 * Time: 03:37
 * To change this template use File | Settings | File Templates.
 */
public class RgbSequencePlayerImpl implements RgbSequencePlayer
{
    protected Map<String, RgbSequence> rgbSequenceMap = new HashMap<>();

    protected RgbSequence currentSequence;

    public RgbSequencePlayerImpl()
    {
    }

    public RgbSequencePlayerImpl(Collection<RgbSequence> rgbSequences)
    {
        Assert.notNull(rgbSequences, "RGB sequence collection can not be null");

        for (RgbSequence rgbSequence : rgbSequences)
            addSequence(rgbSequence);
    }

    @Override
    public void addSequence(RgbSequence rgbSequence)
    {
        Assert.notNull(rgbSequence, "RGB sequence can not be null");
        Assert.hasLength(rgbSequence.getName(), "RGB sequence name can not be null or empty");

        rgbSequenceMap.put(rgbSequence.getName(), rgbSequence);
    }

    protected RgbSequence getSequence(String name) throws RgbSequencePlayerException
    {
        RgbSequence sequence = rgbSequenceMap.get(name);
        if (sequence == null)
            throw new RgbSequencePlayerException("RGB sequence with name \"" + name + "\" not found");
        return sequence;
    }

    @Override
    public void play(String name) throws RgbSequencePlayerException
    {
        currentSequence = getSequence(name);
        currentSequence.start();
    }

    @Override
    public void stop()
    {
        if (currentSequence != null)
        {
            currentSequence.stop();
            currentSequence = null;
        }
    }
}
