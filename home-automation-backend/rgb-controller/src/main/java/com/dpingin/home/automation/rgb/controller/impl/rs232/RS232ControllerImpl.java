package com.dpingin.home.automation.rgb.controller.impl.rs232;

import com.dpingin.home.automation.rgb.controller.api.rs232.RS232Controller;
import com.dpingin.home.automation.rgb.controller.api.rs232.RS232ControllerException;
import com.dpingin.home.automation.rgb.controller.util.HexUtils;
import gnu.io.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 20.11.13
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */
public class RS232ControllerImpl implements SerialPortEventListener, RS232Controller
{
    final static int TIMEOUT = 2000;
    final static int NEW_LINE_ASCII = 10;
    private final Logger log = Logger.getLogger(getClass());
    protected List<Byte> dataRead = new ArrayList();
    private HashMap<String, CommPortIdentifier> portMap = new HashMap();
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    @Override
    public Set<String> enumeratePorts()
    {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                log.info("Found port: " + curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
        return portMap.keySet();
    }

    @Override
    public void connect(String portName) throws RS232ControllerException
    {
        CommPortIdentifier selectedPortIdentifier = portMap.get(portName);

        if (selectedPortIdentifier == null)
            throw new RS232ControllerException("Failed to open port: " + portName);

        try
        {
            CommPort commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
            serialPort = (SerialPort) commPort;

            serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setDTR(false);

            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            log.info(portName + " opened successfully.");
        } catch (PortInUseException e)
        {
            throw new RS232ControllerException(portName + " is in use", e);
        } catch (Exception e)
        {
            throw new RS232ControllerException("Failed to open " + portName, e);
        }
    }

    @Override
    public void disconnect() throws RS232ControllerException
    {
        try
        {
            serialPort.removeEventListener();
            serialPort.close();
            inputStream.close();
            outputStream.close();
            log.info("Disconnected.");
        } catch (IOException e)
        {
            throw new RS232ControllerException("Failed to close " + serialPort.getName(), e);
        }
    }

    @Override
    public void initListener() throws RS232ControllerException
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.notifyOnCarrierDetect(true);
            serialPort.notifyOnCTS(true);
            serialPort.notifyOnDSR(true);
            serialPort.notifyOnFramingError(true);
            serialPort.notifyOnOutputEmpty(true);
            serialPort.notifyOnOverrunError(true);
            serialPort.notifyOnRingIndicator(true);
            serialPort.notifyOnParityError(true);
        } catch (TooManyListenersException e)
        {
            throw new RS232ControllerException("Too many listeners");
        }
    }

    public void serialEvent(SerialPortEvent event)
    {
        switch (event.getEventType())
        {
            case SerialPortEvent.DATA_AVAILABLE:
                log.debug("Data available");
                try
                {
                    byte b = (byte) inputStream.read();

                    synchronized (dataRead)
                    {
                        dataRead.add(b);
                    }

                    if (b != NEW_LINE_ASCII)
                    {
                        log.debug(HexUtils.toHexString(b));
                    } else
                    {
                        log.debug("NEW LINE");
                    }
                } catch (IOException e)
                {
                    throw new RuntimeException("Failed to read data", e);
                }
                break;
            case SerialPortEvent.BI:
                log.trace("Break Interrupt");
                break;
            case SerialPortEvent.CD:
                log.trace("Carrier Detect");
                break;
            case SerialPortEvent.CTS:
                log.trace("CTS");
                break;
            case SerialPortEvent.DSR:
                log.trace("DSR");
                break;
            case SerialPortEvent.FE:
                log.trace("Framing Error");
                break;
            case SerialPortEvent.OE:
                log.trace("Output Empty");
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                log.trace("Output Buffer Empty");
                break;
            case SerialPortEvent.PE:
                log.trace("Parity Error");
                break;
            case SerialPortEvent.RI:
                log.trace("Ring Indicator");
                break;
        }
    }

    @Override
    public void writeData(byte[] data) throws RS232ControllerException
    {
        try
        {
            log.debug("Sending data: " + HexUtils.toHexString(data));

            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e)
        {
            throw new RS232ControllerException("Failed to write data", e);
        }
    }

    @Override
    public byte[] readData() throws RS232ControllerException
    {
        while (true)
        {
            synchronized (dataRead)
            {
                if (dataRead.size() > 0)
                    break;
            }

            try
            {
                Thread.sleep(5);
            } catch (InterruptedException e)
            {
            }
        }
        return readDataNonBlocking();
    }

    @Override
    public byte[] readDataNonBlocking() throws RS232ControllerException
    {
        synchronized (dataRead)
        {
            if (dataRead.size() == 0)
                return null;
            byte[] data = new byte[dataRead.size()];
            for (int i = 0; i < data.length; i++)
                data[i] = dataRead.get(i);
            dataRead.clear();
            return data;
        }
    }
}

