package com.dpingin.home.automation.audio.api.pattern.switcher;

import com.dpingin.home.automation.audio.api.pattern.Pattern;

import java.util.Collection;

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
}
