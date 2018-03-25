package com.dpingin.homeautomation.spice.request;

import android.net.Uri;
import android.util.Log;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.net.URI;

public class SetControlValueRequest extends SpringAndroidSpiceRequest<Void>
{
	private static final String TAG = "SetControlValueRequest";

	private String patternName;
	private String controlName;
	private Object value;

	public SetControlValueRequest(String patternName, String controlName, Object value)
	{
		super(Void.class);
		this.patternName = patternName;
		this.controlName = controlName;
		this.value = value;
	}

	@Override
	public Void loadDataFromNetwork() throws Exception
	{
		Log.d(TAG, String.format("Setting value of pattern's {%s} control {%s} to {%s}", patternName, controlName, value));

		String uriString = new Uri.Builder()
				.scheme("http")
				.encodedAuthority("192.168.1.11:8080")
				.appendPath("led")
				.appendPath("patterns")
				.appendPath(patternName)
				.appendPath("controls")
				.appendPath(controlName)
				.build()
				.toString();

		return getRestTemplate().postForObject(new URI(uriString), value, Void.class);
	}

}
