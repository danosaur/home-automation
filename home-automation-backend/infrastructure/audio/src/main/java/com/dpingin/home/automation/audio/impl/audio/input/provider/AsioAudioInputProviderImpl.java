package com.dpingin.home.automation.audio.impl.audio.input.provider;

import com.dpingin.home.automation.audio.api.audio.input.provider.AbstractAudioInputProvider;
import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProvider;
import com.dpingin.home.automation.audio.api.audio.input.provider.AudioInputProviderException;
import com.dpingin.home.automation.audio.impl.audio.input.AsioAudioInputImpl;
import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverListener;
import com.dpingin.home.automation.audio.api.audio.input.AudioInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void init() throws AudioInputProviderException
    {
        List<String> driverNames = AsioDriver.getDriverNames();

        if (driverNames.size() == 0)
            throw new AudioInputProviderException("No ASIO drivers found");

        StringBuilder stringBuilder = new StringBuilder()
                .append("Found ASIO drivers with names: ")
                .append(System.lineSeparator());
        for (String driverName : driverNames)
        {
            stringBuilder.append(driverName)
                    .append(System.lineSeparator());
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

        audioInput = new AsioAudioInputImpl().asioDriver(asioDriver);
        audioInput.init();

        Set<AsioChannel> activeChannels = new HashSet<AsioChannel>();
        AsioChannel asioChannel = asioDriver.getChannelInput(channelIndex);
        activeChannels.add(asioChannel);

        log.info(String.format("Selected audio channel: %s", asioChannel.getChannelName()));

        asioDriver.createBuffers(activeChannels);

        asioDriver.start();
    }

    @Override
    public void destroy()
    {
        asioDriver.stop();
        asioDriver.shutdownAndUnloadDriver();
    }

    @Override
    public AudioInput getAudioInput()
    {
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
