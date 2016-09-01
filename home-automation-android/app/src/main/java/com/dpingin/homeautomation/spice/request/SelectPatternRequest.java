package com.dpingin.homeautomation.spice.request;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.HashMap;
import java.util.Map;

public class SelectPatternRequest extends SpringAndroidSpiceRequest<Void>
{
	private static final String TAG = "SelectPatternRequest";

	private String patternName;

	public SelectPatternRequest(String patternName)
	{
		super(Void.class);
		this.patternName = patternName;
	}

	@Override
	public Void loadDataFromNetwork() throws Exception
	{
		Log.d(TAG, String.format("selecting pattern: %s", patternName));

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("p", patternName);

		return getRestTemplate().getForObject("http://192.168.1.11:8080/rest-rgb/rest/rgb/pattern?p={p}", Void.class, uriVariables);
	}

}
