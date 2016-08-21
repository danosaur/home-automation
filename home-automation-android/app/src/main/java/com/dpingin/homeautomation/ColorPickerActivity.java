package com.dpingin.homeautomation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dpingin.homeautomation.spice.HomeAutomationSpiceService;
import com.dpingin.homeautomation.spice.request.SetColorRequest;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;

public class ColorPickerActivity extends Activity implements ColorPicker.OnColorChangedListener {
    private static final String TAG = "ColorPickerActivity";

    private SpiceManager spiceManager = new SpiceManager(HomeAutomationSpiceService.class);

    private ColorPicker picker;
    private ValueBar valueBar;
    private SaturationBar saturationBar;
//    private Button button;
//    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);

        picker = (ColorPicker) findViewById(R.id.picker);
        valueBar = (ValueBar) findViewById(R.id.valuebar);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
//        button = (Button) findViewById(R.id.button1);
//        text = (TextView) findViewById(R.id.textView1);

        picker.addValueBar(valueBar);
        picker.addSaturationBar(saturationBar);
        picker.setOnColorChangedListener(this);

//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                text.setTextColor(picker.getColor());
//                picker.setOldCenterColor(picker.getColor());
//            }
//        });
    }

    @Override
    public void onColorChanged(int color) {
        Log.d(TAG, Integer.toString(color));
        picker.setOldCenterColor(color);

        SetColorRequest setColorRequest = new SetColorRequest(color);
        getSpiceManager().execute(setColorRequest, new SetColorRequestListener());
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    public final class SetColorRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(ColorPickerActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {
            Log.d(TAG, result);
        }
    }
}
