package com.dpingin.homeautomation.spice;

import android.app.Application;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBitmapObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple service
 *
 * @author sni
 */
public class HomeAutomationSpiceService extends SpringAndroidSpiceService {
    private static final String TAG = "SpiceService";


    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        return new CacheManager();
    }

    @Override
    public int getThreadCount() {
        return 3;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v(TAG, "Starting service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Starting service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Stopping service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "Bound service");
        return super.onBind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG, "Rebound service");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "Unbound service");
        return super.onUnbind(intent);
    }

    @Override
    public RestTemplate createRestTemplate() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJacksonHttpMessageConverter());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }
}
