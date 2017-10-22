package com.dpingin.home.automation.audio.impl.configuration;

import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProvider;
import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.audio.api.pattern.control.Control;
import com.dpingin.home.automation.audio.api.pattern.switcher.PatternSwitcher;
import com.dpingin.home.automation.audio.impl.pattern.AudioInputColorPattern;
import com.dpingin.home.automation.audio.impl.pattern.StaticColorPattern;
import com.dpingin.home.automation.audio.impl.pattern.control.FloatControl;
import com.dpingin.home.automation.audio.impl.pattern.control.TreeMapControlsImpl;
import com.dpingin.home.automation.audio.impl.pattern.switcher.PatternSwitcherImpl;
import com.dpingin.home.automation.audio.impl.sample.buffer.SampleBufferImpl;
import com.dpingin.home.automation.audio.impl.sample.processor.SpectrumColorSampleProcessor;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.configuration.RgbControllerConfiguration;
import edu.emory.mathcs.backport.java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({RgbControllerConfiguration.class,
		AudioConfiguration.class})
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
		PatternSwitcher patternSwitcher = new PatternSwitcherImpl();

		List<Pattern> patterns = new ArrayList<>();
		patterns.add(minimAudioInputColorPattern());
		patterns.add(staticColorPattern());
		//		patterns.add(blackColorPattern());
		//		patterns.add(strobeColorPattern());
		//		patterns.add(beatDetectorAudioInputColorPattern());
		patternSwitcher.setPatterns(patterns);

		return patternSwitcher;
	}

	@Bean
	public AudioInputColorPattern minimAudioInputColorPattern()
	{
		AudioInputColorPattern minimAudioInputColorPattern = new AudioInputColorPattern();
		minimAudioInputColorPattern.setName("minim");
		minimAudioInputColorPattern.setRgbController(rgbController);
		minimAudioInputColorPattern.setAudioInputProvider(audioInputProvider);
		minimAudioInputColorPattern.setSampleProcessor(spectrumColorSampleProcessor);
		minimAudioInputColorPattern.setSampleBuffer(new SampleBufferImpl()
															.size(bufferSize));
		Control floatControl = new FloatControl();
		minimAudioInputColorPattern.setControls(new TreeMapControlsImpl(Collections.singletonList(new FloatControl()
																										  .name("gain")
																										  .minimumValue(0f)
																										  .maximumValue(1f)
																										  .defaultValue(.4f))));
		return minimAudioInputColorPattern;
	}

	@Bean
	public StaticColorPattern staticColorPattern()
	{
		StaticColorPattern staticColorPattern = new StaticColorPattern();
		staticColorPattern.setName("static");
		staticColorPattern.setRgbController(rgbController);
		return staticColorPattern;
	}
}
