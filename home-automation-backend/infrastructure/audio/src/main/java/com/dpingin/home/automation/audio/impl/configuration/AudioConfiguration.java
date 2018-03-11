package com.dpingin.home.automation.audio.impl.configuration;

import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProvider;
import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import com.dpingin.home.automation.audio.impl.audio.input.provider.AsioAudioInputProviderImpl;
import com.dpingin.home.automation.audio.impl.sample.processor.SpectrumColorSampleProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioConfiguration
{
	@Value("${asio.input.driverName}")
	private String driverName;

	@Value("${asio.input.channelIndex:1}")
	private int channelIndex;

	@Bean
	public AudioInputProvider asioAudioInputProvider()
	{
		AsioAudioInputProviderImpl asioAudioInputProvider = new AsioAudioInputProviderImpl();
		asioAudioInputProvider.setDriverName(driverName);
		asioAudioInputProvider.setChannelIndex(channelIndex);
		return asioAudioInputProvider;
	}

	@Bean
	public SpectrumColorSampleProcessor spectrumColorSampleProcessor()
	{
		return new SpectrumColorSampleProcessor();
	}
}
