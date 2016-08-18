package com.dpingin.home.automation.audio.impl.pattern.switcher;

import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.audio.api.pattern.PatternException;
import com.dpingin.home.automation.audio.api.pattern.switcher.PattenSwitcherException;
import com.dpingin.home.automation.audio.api.pattern.switcher.PatternSwitcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(PatternSwitcherImpl.class);

    protected Map<String, Pattern> patternMap = new HashMap<>();

    protected Pattern currentPattern;

    private Object lock = new Object();

    @Override
    public void addPattern(Pattern pattern)
    {
        Assert.notNull(pattern);
        Assert.hasText(pattern.getName());

        synchronized (lock)
        {
            patternMap.put(pattern.getName(), pattern);
        }
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

        synchronized (lock)
        {
            Pattern pattern = patternMap.get(name);
            if (pattern == null)
                throw new PattenSwitcherException("Pattern not found: " + name);

            log.info(String.format("Switching pattern to: %s...", name));

            if (currentPattern != null)
            {
                log.debug(String.format("Stopping current pattern: %s...", currentPattern.getName()));
                currentPattern.stop();
                log.debug("Current pattern stopped");
            }

            try
            {
                log.debug("Starting new pattern...");
                pattern.start();
                log.debug("New pattern started");
            } catch (PatternException e)
            {
                throw new PattenSwitcherException("Failed to start pattern: " + name, e);
            }

            currentPattern = pattern;
        }
    }

    @Override
    public Pattern getPattern(String name)
    {
        return patternMap.get(name);
    }
}
