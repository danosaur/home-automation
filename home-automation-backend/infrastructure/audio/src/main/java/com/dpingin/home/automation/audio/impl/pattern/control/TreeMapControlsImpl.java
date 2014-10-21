package com.dpingin.home.automation.audio.impl.pattern.control;

import com.dpingin.home.automation.audio.api.pattern.control.Control;
import com.dpingin.home.automation.audio.api.pattern.control.Controls;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class TreeMapControlsImpl extends TreeMap<String, Control> implements Controls
{
    public TreeMapControlsImpl()
    {
    }

    public TreeMapControlsImpl(Collection<? extends Control> collection)
    {
        super();
        if (collection != null)
            for (Control control : collection)
                put(control.getName(), control);
    }

    public TreeMapControlsImpl(Comparator<? super String> comparator)
    {
        super(comparator);
    }

    public TreeMapControlsImpl(Map<? extends String, ? extends Control> m)
    {
        super(m);
    }

    public TreeMapControlsImpl(SortedMap<String, ? extends Control> m)
    {
        super(m);
    }

    @Override
    public <T> T get(Class<T> controlClass, String name)
    {
        return (T)get(name);
    }
}
