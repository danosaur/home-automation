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

public class MusicControlledActivity extends AppCompatActivity
{
	private static final String TAG = "MusicControlledActivity";

	private SpiceManager spiceManager = new SpiceManager(HomeAutomationSpiceService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_controlled);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());
	}

	@Override
	protected void onStart()
	{
		spiceManager.start(this);
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		spiceManager.shouldStop();
		super.onStop();
	}

	protected SpiceManager getSpiceManager()
	{
		return spiceManager;
	}
}
