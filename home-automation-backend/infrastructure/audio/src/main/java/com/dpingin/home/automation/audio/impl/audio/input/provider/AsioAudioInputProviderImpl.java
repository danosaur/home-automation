package com.dpingin.home.automation.audio.impl.audio.input.provider;

import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import com.dpingin.home.automation.audio.api.audio.input.provider.AbstractAudioInputProvider;
import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProviderException;
import com.dpingin.home.automation.audio.impl.audio.input.AsioAudioInputImpl;
import com.synthbot.jasiohost.AsioDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.10.14
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class AsioAudioInputProviderImpl extends AbstractAudioInputProvider
{
	private final static Logger log = LoggerFactory.getLogger(MinimAudioInputProviderImpl.class);

	protected String driverName;

	protected int channelIndex;

	protected AsioDriver asioDriver;

	@Override
	@PostConstruct
	public void init() throws AudioInputProviderException
	{
		List<String> driverNames = AsioDriver.getDriverNames();

		if (driverNames.size() == 0)
			throw new AudioInputProviderException("No ASIO drivers found");

		StringBuilder stringBuilder = new StringBuilder().append("Found ASIO drivers with names: ").append(System.lineSeparator());
		for (String driverName : driverNames)
		{
			stringBuilder.append(driverName).append(System.lineSeparator());
		}
		log.info(stringBuilder.toString());

		if (StringUtils.hasLength(driverName))
		{
			boolean found = false;
			for (String foundDriverName : driverNames)
			{
				{
					if (driverName.equals(foundDriverName))
					{
						found = true;
						break;
					}
				}
			}
			if (!found)
				throw new AudioInputProviderException(String.format("Driver with name %s not found", driverName));
		}
		else
			driverName = driverNames.get(0);

		log.info(String.format("Selected audio driver: %s", driverName));

		asioDriver = AsioDriver.getDriver(driverName);
	}

	@Override
	@PreDestroy
	public void destroy()
	{
		if (asioDriver != null)
		{
			asioDriver.stop();
			asioDriver.shutdownAndUnloadDriver();
			asioDriver = null;
		}
	}

	@Override
	public AudioInput getAudioInput()
	{
		audioInput = new AsioAudioInputImpl().asioDriver(asioDriver).channelIndex(channelIndex);
		audioInput.init();
		return audioInput;
	}

	public void setDriverName(String driverName)
	{
		this.driverName = driverName;
	}

	public AsioAudioInputProviderImpl driverName(final String driverName)
	{
		this.driverName = driverName;
		return this;
	}

	public void setChannelIndex(int channelIndex)
	{
		this.channelIndex = channelIndex;
	}

	public AsioAudioInputProviderImpl channelIndex(final int channelIndex)
	{
		this.channelIndex = channelIndex;
		return this;
	}


}
