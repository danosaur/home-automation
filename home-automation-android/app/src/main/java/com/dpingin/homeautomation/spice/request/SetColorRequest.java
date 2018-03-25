package com.dpingin.homeautomation.spice.request;

import android.net.Uri;
import android.util.Log;
import com.dpingin.homeautomation.types.Color;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.net.URI;

public class SetColorRequest extends SpringAndroidSpiceRequest<Color>
{
	private static final String TAG = "SetColorRequest";

	private Color color;

	public SetColorRequest(Color color)
	{
		super(Color.class);
		this.color = color;
	}

	@Override
	public Color loadDataFromNetwork() throws Exception
	{
		Log.d(TAG, String.format("r: %d, g: %d, b: %d", color.getRed(), color.getGreen(), color.getBlue()));

		String uriString = new Uri.Builder()
				.scheme("http")
				.encodedAuthority("192.168.1.11:8080")
				.appendPath("led")
				.appendPath("rgb")
				.build()
				.toString();

		return getRestTemplate().postForObject(new URI(uriString), color, Color.class);
	}

}
