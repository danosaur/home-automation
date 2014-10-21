package com.dpingin.home.automation.audio.impl.pattern.switcher;

import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.audio.api.pattern.switcher.PattenSwitcherException;
import com.dpingin.home.automation.audio.api.pattern.switcher.PatternSwitcher;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 04:46
 * To change this template use File | Settings | File Templates.
 */
public class PatternSwitcherImpl implements PatternSwitcher
{
    protected Map<String, Pattern> patternMap = new HashMap<>();

    protected Pattern currentPattern;

    @Override
    public void addPattern(Pattern pattern)
    {
        Assert.notNull(pattern);
        Assert.hasText(pattern.getName());

        patternMap.put(pattern.getName(), pattern);
    }

    @Override
    public void setPatterns(Collection<Pattern> patterns)
    {
        for (Pattern pattern : patterns)
            addPattern(pattern);
    }

    @Override
    public void switchPattern(String name) throws PattenSwitcherException
    {
        Assert.hasText(name);

        Pattern pattern = patternMap.get(name);
        if (pattern == null)
            throw new PattenSwitcherException("Pattern not found: " + name);

        if (currentPattern != null)
            currentPattern.stop();

        pattern.start();

        currentPattern = pattern;
    }

    @Override
    public Pattern getPattern(String name)
    {
        return patternMap.get(name);
    }
}
