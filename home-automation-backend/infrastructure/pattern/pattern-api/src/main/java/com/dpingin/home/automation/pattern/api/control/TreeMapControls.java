package com.dpingin.home.automation.pattern.api.control;

import org.springframework.util.Assert;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class TreeMapControls extends TreeMap<String, Control> implements Controls
{
	public TreeMapControls()
	{
	}

	public TreeMapControls(Collection<? extends Control> collection)
	{
		super();
		if (collection != null)
			for (Control control : collection)
				put(control.getName(), control);
	}

	public TreeMapControls(Comparator<? super String> comparator)
	{
		super(comparator);
	}

	public TreeMapControls(Map<? extends String, ? extends Control> m)
	{
		super(m);
	}

	public TreeMapControls(SortedMap<String, ? extends Control> m)
	{
		super(m);
	}

	@Override
	public <T> T get(Class<T> controlClass, String controlName)
	{
		Assert.notNull(controlClass, "Control class can not be null");
		Assert.hasLength(controlName, "Control name can not be null or empty");

		return (T)get(controlName);
	}

	@Override
	public void add(Control... controls)
	{
		Assert.notNull(controls, "Control can not be null");

		Arrays.stream(controls)
			.forEach(control -> put(control.getName(), control));
	}
}
