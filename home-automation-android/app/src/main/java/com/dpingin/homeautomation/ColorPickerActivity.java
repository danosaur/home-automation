package com.dpingin.homeautomation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.dpingin.homeautomation.spice.HomeAutomationSpiceService;
import com.dpingin.homeautomation.spice.request.GetColorRequest;
import com.dpingin.homeautomation.spice.request.SetColorRequest;
import com.dpingin.homeautomation.spice.request.manager.RequestManager;
import com.dpingin.homeautomation.types.Color;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ColorPickerActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener, ValueBar.OnValueChangedListener, SaturationBar.OnSaturationChangedListener
{
	private static final String TAG = "ColorPickerActivity";

	private SpiceManager spiceManager = new SpiceManager(HomeAutomationSpiceService.class);

	private ColorPicker picker;
	private ValueBar valueBar;
	private SaturationBar saturationBar;

	private RequestManager setColorRequestManager;
	private RequestManager getColorRequestManager;

	private SetColorRequestListener setColorRequestListener = new SetColorRequestListener();
	private GetColorRequestListener getColorRequestListener = new GetColorRequestListener();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_colorpicker);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		picker = (ColorPicker) findViewById(R.id.picker);
		valueBar = (ValueBar) findViewById(R.id.valuebar);
		valueBar.setOnValueChangedListener(this);
		saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		saturationBar.setOnSaturationChangedListener(this);

		picker.addValueBar(valueBar);
		picker.addSaturationBar(saturationBar);
		picker.setOnColorChangedListener(this);
		picker.setTouchAnywhereOnColorWheelEnabled(true);
	}

	@Override
	public void onColorChanged(int color)
	{
		Log.d(TAG, Integer.toString(color));
		picker.setOldCenterColor(color);

		setColorRequestManager.submit(new SetColorRequest(color), setColorRequestListener);
	}

	@Override
	public void onSaturationChanged(int saturation)
	{
		int color = picker.getColor();

		Log.d(TAG, Integer.toString(color));
		picker.setOldCenterColor(color);

		setColorRequestManager.submit(new SetColorRequest(color), setColorRequestListener);
	}

	@Override
	public void onValueChanged(int value)
	{
		int color = picker.getColor();

		Log.d(TAG, Integer.toString(color));
		picker.setOldCenterColor(color);

		setColorRequestManager.submit(new SetColorRequest(color), setColorRequestListener);
	}


	@Override
	protected void onStart()
	{
		spiceManager.start(this);
		setColorRequestManager = new RequestManager(spiceManager)
				.start();
		getColorRequestManager = new RequestManager(spiceManager)
				.start();
		getColorRequestManager.submit(new GetColorRequest(), getColorRequestListener);
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		setColorRequestManager.stop();
		getColorRequestManager.stop();
		spiceManager.shouldStop();
		super.onStop();
	}

	protected SpiceManager getSpiceManager()
	{
		return spiceManager;
	}

	public final class SetColorRequestListener implements RequestListener<String>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Toast.makeText(ColorPickerActivity.this, "failure", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final String result)
		{
			Log.d(TAG, result);
		}
	}

	public final class GetColorRequestListener implements RequestListener<Color>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Toast.makeText(ColorPickerActivity.this, "failure", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final Color color)
		{
			picker.setColor(color.getColor());
			picker.setOldCenterColor(color.getColor());
		}
	}
}
