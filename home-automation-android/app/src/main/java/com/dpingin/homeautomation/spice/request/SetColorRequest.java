package com.dpingin.homeautomation.spice.request;

import android.util.Log;
import android.widget.Toast;

import com.dpingin.homeautomation.ColorPickerActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.HashMap;
import java.util.Map;

public class SetColorRequest extends SpringAndroidSpiceRequest<String>
{
	private static final String TAG = "SetColorRequest";

	private int color;

	public SetColorRequest(int color)
	{
		super(String.class);
		this.color = color;
	}

	@Override
	public String loadDataFromNetwork() throws Exception
	{

		int r = (0x00ff0000 & color) >> 16;
		int g = (0x0000ff00 & color) >> 8;
		int b = (0x000000ff & color);

		Log.d(TAG, String.format("r: %d, g: %d, b: %d", r, g, b));

		Map<String, Integer> uriVariables = new HashMap<>();
		uriVariables.put("r", r);
		uriVariables.put("g", g);
		uriVariables.put("b", b);

		return getRestTemplate().postForObject("http://192.168.1.11:8080/rest-rgb/rest/rgb/rgb?r={r}&g={g}&b={b}", null, String.class, uriVariables);
	}

}
