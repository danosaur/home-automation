package com.dpingin.homeautomation.spice.request;

import android.net.Uri;
import android.util.Log;
import com.dpingin.homeautomation.types.ControlValueResponse;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.net.URI;

public class GetControlValueRequest extends SpringAndroidSpiceRequest<ControlValueResponse>
{
	private static final String TAG = "GetControlValueRequest";

	private String patternName;
	private String controlName;

	public GetControlValueRequest(String patternName, String controlName)
	{
		super(ControlValueResponse.class);
		this.patternName = patternName;
		this.controlName = controlName;
	}

	@Override
	public ControlValueResponse loadDataFromNetwork() throws Exception
	{
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

		Object value = getRestTemplate().getForObject(new URI(uriString), Object.class);

		Log.d(TAG, String.format("Get value of pattern's {%s} control {%s}: {%s}", patternName, controlName, value));

		return new ControlValueResponse(patternName, controlName, value);
	}

}
