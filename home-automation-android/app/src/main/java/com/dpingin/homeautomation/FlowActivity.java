package com.dpingin.homeautomation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;
import com.dpingin.homeautomation.spice.HomeAutomationSpiceService;
import com.dpingin.homeautomation.spice.request.GetColorRequest;
import com.dpingin.homeautomation.spice.request.GetControlValueRequest;
import com.dpingin.homeautomation.spice.request.SetControlValueRequest;
import com.dpingin.homeautomation.spice.request.manager.RequestManager;
import com.dpingin.homeautomation.types.Color;
import com.dpingin.homeautomation.types.ControlValueResponse;
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

public class FlowActivity extends AppCompatActivity implements ValueBar.OnValueChangedListener, SaturationBar.OnSaturationChangedListener
{
	private static final String TAG = "FlowActivity";
	private static final String PATTERN_NAME = "flow";
	private static final String CONTROL_NAME_SPEED = "hueFullCycleTime";
	private static final String CONTROL_NAME_VALUE = "value";
	private static final String CONTROL_NAME_SATURATION = "saturation";
	private static final int MAX_SPEED = 200;

	private SpiceManager spiceManager = new SpiceManager(HomeAutomationSpiceService.class);

	private SeekBar speedBar;
	private ColorPicker colorPicker;
	private ValueBar valueBar;
	private SaturationBar saturationBar;

	private Color currentColor = new Color(255, 0, 0);

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduledFuture;

	private RequestManager setControlValueRequestManager;
	private RequestManager getColorRequestManager;

	private SetControlValueRequestListener setControlValueRequestListener = new SetControlValueRequestListener();
	private GetControlValueRequestListener getControlValueRequestListener = new GetControlValueRequestListener();
	private GetColorRequestListener getColorRequestListener = new GetColorRequestListener();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flow);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		speedBar = (SeekBar) findViewById(R.id.seekBarSpeed);
		speedBar.setMax(MAX_SPEED);
		speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				setControlValueRequestManager.submit(new SetControlValueRequest(PATTERN_NAME, CONTROL_NAME_SPEED, (MAX_SPEED - progress) * 1000), setControlValueRequestListener);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}
		});

		valueBar = (ValueBar) findViewById(R.id.valuebar);
		valueBar.setOnValueChangedListener(this);

		saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		saturationBar.setOnSaturationChangedListener(this);

		colorPicker = (ColorPicker) findViewById(R.id.picker);
		colorPicker.addValueBar(valueBar);
		colorPicker.addSaturationBar(saturationBar);
		colorPicker.setTouchAnywhereOnColorWheelEnabled(true);
		colorPicker.setShowOldCenterColor(false);
	}

	@Override
	protected void onStart()
	{
		spiceManager.start(this);

		setControlValueRequestManager = new RequestManager(spiceManager).start();
		getColorRequestManager = new RequestManager(spiceManager).start();

		scheduledFuture = scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()
			{
				getColorRequestManager.submit(new GetColorRequest(), getColorRequestListener);
			}
		}, 0, 1, TimeUnit.SECONDS);

		spiceManager.execute(new GetControlValueRequest(PATTERN_NAME, CONTROL_NAME_SPEED), getControlValueRequestListener);
		spiceManager.execute(new GetControlValueRequest(PATTERN_NAME, CONTROL_NAME_VALUE), getControlValueRequestListener);
		spiceManager.execute(new GetControlValueRequest(PATTERN_NAME, CONTROL_NAME_SATURATION), getControlValueRequestListener);

		super.onStart();
	}

	@Override
	protected void onStop()
	{
		scheduledFuture.cancel(true);

		setControlValueRequestManager.stop();
		getColorRequestManager.stop();

		spiceManager.shouldStop();

		super.onStop();
	}

	protected SpiceManager getSpiceManager()
	{
		return spiceManager;
	}

	@Override
	public void onSaturationChanged(int saturation)
	{
		Color color = new Color(colorPicker.getColor());
		setControlValueRequestManager.submit(new SetControlValueRequest(PATTERN_NAME, CONTROL_NAME_SATURATION, color.getSaturation()), setControlValueRequestListener);
	}

	@Override
	public void onValueChanged(int value)
	{
		Color color = new Color(colorPicker.getColor());
		setControlValueRequestManager.submit(new SetControlValueRequest(PATTERN_NAME, CONTROL_NAME_VALUE, color.getValue()), setControlValueRequestListener);
	}

	public final class SetControlValueRequestListener implements RequestListener<Void>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Toast.makeText(FlowActivity.this, "failure", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final Void result)
		{
			Log.d(TAG, "Set control value OK");
		}
	}

	public final class GetControlValueRequestListener implements RequestListener<ControlValueResponse>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Toast.makeText(FlowActivity.this, "failure", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final ControlValueResponse controlValueResponse)
		{
			if (!PATTERN_NAME.equals(controlValueResponse.getPatternName()))
				return;

			switch (controlValueResponse.getControlName())
			{
				case CONTROL_NAME_SPEED:
					speedBar.setProgress(MAX_SPEED - (int) controlValueResponse.getValue() / 1000);
					break;
				case CONTROL_NAME_VALUE:
					float value = (float) ((double) controlValueResponse.getValue());
					currentColor.setValue(value);
					colorPicker.setColor(colorPicker.getColor());
					colorPicker.setOldCenterColor(colorPicker.getColor());
					break;
				case CONTROL_NAME_SATURATION:
					float saturation = (float) ((double) controlValueResponse.getValue());
					currentColor.setSaturation(saturation);
					colorPicker.setColor(colorPicker.getColor());
					colorPicker.setOldCenterColor(colorPicker.getColor());
					break;
			}
		}
	}

	public final class GetColorRequestListener implements RequestListener<Color>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Toast.makeText(FlowActivity.this, "failure", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final Color color)
		{
			colorPicker.setColor(color.getColor());
		}
	}
}
