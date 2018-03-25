package com.dpingin.home.automation.pattern.impl.configuration;

import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProvider;
import com.dpingin.home.automation.audio.impl.configuration.AudioConfiguration;
import com.dpingin.home.automation.audio.impl.sample.buffer.DefaultSampleBuffer;
import com.dpingin.home.automation.audio.impl.sample.processor.SpectrumColorSampleProcessor;
import com.dpingin.home.automation.pattern.api.Pattern;
import com.dpingin.home.automation.pattern.api.switcher.PatternSwitcher;
import com.dpingin.home.automation.pattern.impl.AudioInputColorPattern;
import com.dpingin.home.automation.pattern.impl.FlowColorPattern;
import com.dpingin.home.automation.pattern.impl.StaticColorPattern;
import com.dpingin.home.automation.pattern.impl.switcher.DefaultPatternSwitcher;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.configuration.RgbControllerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({RgbControllerConfiguration.class, AudioConfiguration.class})
public class PatternSwitcherConfiguration
{
	@Value("#{${pattern.audioInputColorPattern.bufferSize}}")
	private int bufferSize;

	private RgbController rgbController;

	private AudioInputProvider audioInputProvider;

	private SpectrumColorSampleProcessor spectrumColorSampleProcessor;

	@Autowired
	public PatternSwitcherConfiguration(RgbController rgbController, AudioInputProvider audioInputProvider, SpectrumColorSampleProcessor spectrumColorSampleProcessor)
	{
		this.rgbController = rgbController;
		this.audioInputProvider = audioInputProvider;
		this.spectrumColorSampleProcessor = spectrumColorSampleProcessor;
	}

	@Bean
	public PatternSwitcher patternSwitcher()
	{
		PatternSwitcher patternSwitcher = new DefaultPatternSwitcher();

		List<Pattern> patterns = new ArrayList<>();
		patterns.add(minimAudioInputColorPattern());
		patterns.add(staticColorPattern());
		patterns.add(flowColorPattern());
		patternSwitcher.setPatterns(patterns);

		return patternSwitcher;
	}

	@Bean
	public AudioInputColorPattern minimAudioInputColorPattern()
	{
		AudioInputColorPattern pattern = new AudioInputColorPattern();
		pattern.setName("minim");
		pattern.setRgbController(rgbController);
		pattern.setAudioInputProvider(audioInputProvider);
		pattern.setSampleProcessor(spectrumColorSampleProcessor);
		pattern.setSampleBuffer(new DefaultSampleBuffer()
			.size(bufferSize));
		return pattern;
	}

	@Bean
	public StaticColorPattern staticColorPattern()
	{
		StaticColorPattern staticColorPattern = new StaticColorPattern();
		staticColorPattern.setName("static");
		staticColorPattern.setRgbController(rgbController);
		return staticColorPattern;
	}

	@Bean
	public FlowColorPattern flowColorPattern()
	{
		FlowColorPattern pattern = new FlowColorPattern();
		pattern.setName("flow");
		pattern.setRgbController(rgbController);
		return pattern;
	}
}
