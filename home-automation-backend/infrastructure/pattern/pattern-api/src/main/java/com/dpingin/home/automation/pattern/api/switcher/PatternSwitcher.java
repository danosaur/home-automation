package com.dpingin.home.automation.pattern.api.switcher;

import com.dpingin.home.automation.pattern.api.Pattern;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 04:42
 * To change this template use File | Settings | File Templates.
 */
public interface PatternSwitcher
{
	void addPattern(Pattern pattern);

	void setPatterns(Collection<Pattern> patterns);

	void switchPattern(String name) throws PattenSwitcherException;

	Pattern getPattern(String name);

	Pattern getCurrentPattern();

	List<Pattern> getPatterns();
}
