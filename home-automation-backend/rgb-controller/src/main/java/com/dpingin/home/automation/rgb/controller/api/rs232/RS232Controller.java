package com.dpingin.home.automation.rgb.controller.api.rs232;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 21.11.13
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
public interface RS232Controller
{
    Set<String> enumeratePorts();
    void connect(String portName) throws RS232ControllerException;
    void initListener() throws RS232ControllerException;
    void disconnect() throws RS232ControllerException;

    void writeData(byte[] data) throws RS232ControllerException;
    byte[] readData() throws RS232ControllerException;
    byte[] readDataNonBlocking() throws RS232ControllerException;
}
