package com.dpingin.homeautomation.spice.request;

import android.util.Log;

import com.dpingin.homeautomation.types.Color;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.HashMap;
import java.util.Map;

public class GetColorRequest extends SpringAndroidSpiceRequest<Color>
{
	private static final String TAG = "GetColorRequest";

	public GetColorRequest()
	{
		super(Color.class);
	}

	@Override
	public Color loadDataFromNetwork() throws Exception
	{
		Color c = getRestTemplate().getForObject("http://192.168.1.11:8080/rest-rgb/rest/rgb/rgb", Color.class);

		Log.d(TAG, String.format("r: %d, g: %d, b: %d", c.getRed(), c.getGreen(), c.getBlue()));

		return c;
	}

}
