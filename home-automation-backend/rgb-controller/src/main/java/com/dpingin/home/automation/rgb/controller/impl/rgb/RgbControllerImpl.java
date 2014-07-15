package com.dpingin.home.automation.rgb.controller.impl.rgb;

import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import com.dpingin.home.automation.rgb.controller.api.rs232.RS232Controller;
import com.dpingin.home.automation.rgb.controller.api.rs232.RS232ControllerException;
import com.dpingin.home.automation.rgb.controller.util.CrcUtils;
import com.dpingin.home.automation.rgb.controller.util.HexUtils;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 10.06.14
 * Time: 20:21
 * To change this template use File | Settings | File Templates.
 */
public class RgbControllerImpl implements RgbController
{
    protected String portName;
    protected RS232Controller rs232Controller;
    protected Color color = Color.BLACK;

    public void init() throws RgbControllerException
    {
        try
        {
            Set<String> ports = rs232Controller.enumeratePorts();
            if (!ports.contains(portName))
            {
                StringBuilder stringBuilder = new StringBuilder().append("Port ")
                        .append(portName)
                        .append(" not found. Available ports:")
                        .append(System.lineSeparator());
                for (String port : ports)
                {
                    stringBuilder.append(port)
                            .append(System.lineSeparator());
                }

                throw new RgbControllerException(stringBuilder.toString());
            }
            rs232Controller.connect(portName);

            rs232Controller.initListener();

            sync();

        } catch (RS232ControllerException e)
        {
            throw new RgbControllerException("Failed to initialize RGB controller", e);
        }
    }

    public void destroy()
    {
        try
        {
            rs232Controller.disconnect();
        } catch (RS232ControllerException e)
        {
        }
    }

    @Override
    public Color getColor()
    {
        return color;
    }

    @Override
    public void setColor(Color color) throws RgbControllerException
    {
        Assert.notNull(color, "Color can not be null");

        setColor(color.getR(), color.getG(), color.getB());
    }

    @Override
    public void setColor(int r, int g, int b) throws RgbControllerException
    {
        Assert.state(r >= 0 && r < 256, "R must be in range [0..255]");
        Assert.state(g >= 0 && g < 256, "G must be in range [0..255]");
        Assert.state(b >= 0 && b < 256, "B must be in range [0..255]");

        try
        {
            byte[] data = {0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            data[2] = (byte) ((byte) r & 0xFF);
            data[3] = (byte) ((byte) g & 0xFF);
            data[4] = (byte) ((byte) b & 0xFF);

            byte[] crc16 = CrcUtils.calculateCrc16(data);


            synchronized (rs232Controller)
            {
                rs232Controller.readDataNonBlocking();
                rs232Controller.writeData(data);
                rs232Controller.writeData(crc16);
                data = rs232Controller.readData();

                if (data != null && data[data.length - 1] == 0x61)
                    this.color = new Color(r, g, b);
                else
                    sync();
            }
        } catch (RS232ControllerException e)
        {
            throw new RgbControllerException("Failed to set color", e);
        }
    }

    private void sync() throws RS232ControllerException
    {
        for (int i = 0; i < 100; i++)
        {
            rs232Controller.writeData(HexUtils.fromHexString("FD"));
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
            }

            byte[] data = rs232Controller.readDataNonBlocking();
            if (data != null && (data[0] == 0x0E || data[0] == 0x65))
                break;
        }

        rs232Controller.writeData(HexUtils.fromHexString("FD 00 00 00 00 00 00 CF 2C"));
    }

    private void sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
        }
    }

    public void setRs232Controller(RS232Controller rs232Controller)
    {
        this.rs232Controller = rs232Controller;
    }

    public void setPortName(String portName)
    {
        this.portName = portName;
    }
}
