package com.dpingin.home.automation.pattern.impl.switcher;

import com.dpingin.home.automation.pattern.api.Pattern;
import com.dpingin.home.automation.pattern.api.PatternException;
import com.dpingin.home.automation.pattern.api.switcher.PattenSwitcherException;
import com.dpingin.home.automation.pattern.api.switcher.PatternSwitcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 04:46
 * To change this template use File | Settings | File Templates.
 */
public class DefaultPatternSwitcher implements PatternSwitcher
{
	private static final Logger log = LoggerFactory.getLogger(DefaultPatternSwitcher.class);

	protected Map<String, Pattern> patternMap = new LinkedHashMap<>();

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
			}
			catch (PatternException e)
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

	@Override
	public Pattern getCurrentPattern()
	{
		return currentPattern;
	}

	@Override
	public List<Pattern> getPatterns()
	{
		return patternMap.entrySet()
			.stream()
			.map(Map.Entry::getValue)
			.collect(Collectors.toList());
	}
}
