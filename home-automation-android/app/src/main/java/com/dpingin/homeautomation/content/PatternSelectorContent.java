package com.dpingin.homeautomation.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternSelectorContent
{
	public static final List<PatternSelectorItem> ITEMS = new ArrayList<>();

	public static final Map<String, PatternSelectorItem> ITEM_MAP = new HashMap<>();

	static
	{
		addItem(createItem("static", "Static Color", "Set static color using color picker"));
	}

	private static void addItem(PatternSelectorItem item)
	{
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	private static PatternSelectorItem createItem(String id, String content, String details)
	{
		return new PatternSelectorItem(id, content, details);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class PatternSelectorItem
	{
		public final String id;
		public final String content;
		public final String details;

		public PatternSelectorItem(String id, String content, String details)
		{
			this.id = id;
			this.content = content;
			this.details = details;
		}

		@Override
		public String toString()
		{
			return content;
		}
	}
}
