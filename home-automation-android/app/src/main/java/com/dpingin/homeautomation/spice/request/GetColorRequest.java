package com.dpingin.homeautomation.spice.request;

import android.net.Uri;
import android.util.Log;
import com.dpingin.homeautomation.types.Color;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.net.URI;

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
		String uriString = new Uri.Builder()
				.scheme("http")
				.encodedAuthority("192.168.1.11:8080")
				.appendPath("led")
				.appendPath("rgb")
				.build()
				.toString();

		Color color = getRestTemplate().getForObject(new URI(uriString), Color.class);

		Log.d(TAG, String.format("Get color: %d, %d, %d", color.getRed(), color.getGreen(), color.getBlue()));

		return color;
	}

}
