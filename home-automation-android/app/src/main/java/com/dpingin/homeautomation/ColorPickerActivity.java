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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ColorPickerActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener, ValueBar.OnValueChangedListener, SaturationBar.OnSaturationChangedListener
{
	private static final String TAG = "ColorPickerActivity";

	private SpiceManager spiceManager = new SpiceManager(HomeAutomationSpiceService.class);

	private ColorPicker colorPicker;
	private ValueBar valueBar;
	private SaturationBar saturationBar;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduledFuture;

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

		valueBar = (ValueBar) findViewById(R.id.valuebar);
		valueBar.setOnValueChangedListener(this);
		saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		saturationBar.setOnSaturationChangedListener(this);

		colorPicker = (ColorPicker) findViewById(R.id.picker);
		colorPicker.addValueBar(valueBar);
		colorPicker.addSaturationBar(saturationBar);
		colorPicker.setOnColorChangedListener(this);
		colorPicker.setTouchAnywhereOnColorWheelEnabled(true);
		colorPicker.setShowOldCenterColor(false);
	}

	@Override
	public void onColorChanged(int color)
	{
		Log.d(TAG, Integer.toString(color));

		setColorRequestManager.submit(new SetColorRequest(new Color(color)), setColorRequestListener);
	}

	@Override
	public void onSaturationChanged(int saturation)
	{
		int color = colorPicker.getColor();

		Log.d(TAG, Integer.toString(color));
		colorPicker.setOldCenterColor(color);

		setColorRequestManager.submit(new SetColorRequest(new Color(color)), setColorRequestListener);
	}

	@Override
	public void onValueChanged(int value)
	{
		int color = colorPicker.getColor();

		Log.d(TAG, Integer.toString(color));
		colorPicker.setOldCenterColor(color);

		setColorRequestManager.submit(new SetColorRequest(new Color(color)), setColorRequestListener);
	}


	@Override
	protected void onStart()
	{
		spiceManager.start(this);

		setColorRequestManager = new RequestManager(spiceManager).start();
		getColorRequestManager = new RequestManager(spiceManager).start();

		scheduledFuture = scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()
			{
				getColorRequestManager.submit(new GetColorRequest(), getColorRequestListener);
			}
		}, 0, 1, TimeUnit.SECONDS);

		super.onStart();
	}

	@Override
	protected void onStop()
	{
		scheduledFuture.cancel(true);

		setColorRequestManager.stop();
		getColorRequestManager.stop();

		spiceManager.shouldStop();
		
		super.onStop();
	}

	protected SpiceManager getSpiceManager()
	{
		return spiceManager;
	}

	public final class SetColorRequestListener implements RequestListener<Color>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Toast.makeText(ColorPickerActivity.this, "failure", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final Color result)
		{
			Log.d(TAG, "Set color: " + result);
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
			colorPicker.setOnColorChangedListener(null);
			colorPicker.setColor(color.getColor());
			colorPicker.setOnColorChangedListener(ColorPickerActivity.this);
		}
	}
}
