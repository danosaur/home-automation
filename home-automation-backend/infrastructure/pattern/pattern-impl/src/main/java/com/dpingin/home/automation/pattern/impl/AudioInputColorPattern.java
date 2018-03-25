package com.dpingin.home.automation.pattern.impl;

import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import com.dpingin.home.automation.audio.api.audio.input.AudioInputException;
import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProvider;
import com.dpingin.home.automation.audio.api.sample.buffer.SampleBuffer;
import com.dpingin.home.automation.audio.api.sample.processor.SampleProcessor;
import com.dpingin.home.automation.audio.impl.sample.processor.output.ColorSampleProcessorOutput;
import com.dpingin.home.automation.pattern.api.PatternException;
import com.dpingin.home.automation.pattern.api.control.Controllable;
import com.dpingin.home.automation.pattern.api.control.FloatControl;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import ddf.minim.AudioListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 27.09.14
 * Time: 01:29
 * To change this template use File | Settings | File Templates.
 */
public class AudioInputColorPattern extends SampleProcessorAwarePattern<SampleProcessor<ColorSampleProcessorOutput>>
{
	private final static Logger log = LoggerFactory.getLogger(AudioInputColorPattern.class);

	protected AudioInputProvider audioInputProvider;

	protected AudioInput audioInput;

	protected SampleBuffer sampleBuffer;

	protected AudioListener audioListener;

	@Controllable
	private FloatControl gainControl;

	@Override
	@PostConstruct
	public void init()
	{
		Assert.notNull(audioInputProvider, "Audio input provider can not be null");
		Assert.notNull(sampleBuffer, "Sample buffer can not be null");

		gainControl = new FloatControl()
					.name("gain")
					.minimumValue(0f)
					.maximumValue(1f)
					.defaultValue(.4f);

		controls.add(gainControl);

		super.init();

		audioInput = audioInputProvider.getAudioInput();
	}

	@Override
	@PreDestroy
	public void destroy()
	{
		stop();

		if (audioInput != null)
		{
			audioInput.destroy();
			audioInput = null;
		}
		super.destroy();
	}

	@Override
	public void start() throws PatternException
	{
		try
		{
			float audioInputBufferSize = audioInput.getBufferSize();
			log.debug("Audio input buffer size {}", audioInputBufferSize);

			float sampleRate = audioInput.getSampleRate();
			log.debug("Audio input sample rate {}", sampleRate);

			audioListener = new AudioListener()
			{
				@Override
				public void samples(float[] samples)
				{
					sampleBuffer.addSamples(samples);
				}

				@Override
				public void samples(float[] sampL, float[] sampR)
				{
				}
			};
			audioInput.addListener(audioListener);

			audioInput.start();
		}
		catch (AudioInputException e)
		{
			throw new PatternException("Failed to start pattern", e);
		}

		super.start();
	}

	@Override
	public void stop()
	{
		super.stop();

		if (audioInput != null)
		{
			audioInput.stop();
			audioInput.removeListener(audioListener);
		}

		if (sampleBuffer != null)
			sampleBuffer.reset();

		try
		{
			rgbController.setColor(Color.BLACK);
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}
	}

	@Override
	protected void generatePattern()
	{
		try
		{
			float[] samples = sampleBuffer.getSamples();
			if (samples == null)
			{
				rgbController.setColor(Color.BLACK);
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
				}
			}
			else
			{
				float sampleRate = audioInput.getSampleRate();

				ColorSampleProcessorOutput colorSampleProcessorOutput = sampleProcessor.processSamples(samples, sampleRate);

				rgbController.setColor(colorSampleProcessorOutput.getValue());
			}
		}
		catch (RgbControllerException e)
		{
			log.error("Failed to set color", e);
		}
		catch (AudioInputException e)
		{
			log.error("Failed to process samples", e);
		}
	}

	public void setAudioInput(AudioInput audioInput)
	{
		this.audioInput = audioInput;
	}

	public AudioInputColorPattern audioInput(final AudioInput audioInput)
	{
		this.audioInput = audioInput;
		return this;
	}

	public AudioInputColorPattern sampleProcessor(final SampleProcessor<ColorSampleProcessorOutput> sampleProcessor)
	{
		this.sampleProcessor = sampleProcessor;
		return this;
	}

	public void setSampleBuffer(SampleBuffer sampleBuffer)
	{
		this.sampleBuffer = sampleBuffer;
	}

	public AudioInputColorPattern sampleBuffer(final SampleBuffer sampleBuffer)
	{
		this.sampleBuffer = sampleBuffer;
		return this;
	}

	public void setAudioInputProvider(AudioInputProvider audioInputProvider)
	{
		this.audioInputProvider = audioInputProvider;
	}

	public AudioInputColorPattern audioInputProvider(final AudioInputProvider audioInputProvider)
	{
		this.audioInputProvider = audioInputProvider;
		return this;
	}
}
