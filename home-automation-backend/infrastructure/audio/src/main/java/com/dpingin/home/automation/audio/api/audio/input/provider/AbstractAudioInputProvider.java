package com.dpingin.home.automation.audio.api.audio.input.provider;

import com.dpingin.home.automation.audio.api.audio.input.AudioInput;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 23:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAudioInputProvider implements AudioInputProvider
{
	protected AudioInput audioInput;

	public void init() throws AudioInputProviderException
	{

	}

	public void destroy() throws AudioInputProviderException
	{

	}

	@Override
	public AudioInput getAudioInput()
	{
		return audioInput;
	}
}
