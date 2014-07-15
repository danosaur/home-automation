package com.dpingin.home.automation.rgb.controller.test;

import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import com.dpingin.home.automation.rgb.controller.api.rs232.RS232Controller;
import com.dpingin.home.automation.rgb.controller.api.rs232.RS232ControllerException;
import com.dpingin.home.automation.rgb.controller.impl.rs232.RS232ControllerImpl;
import com.dpingin.home.automation.rgb.controller.util.HexUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 20.11.13
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:RgbControllerImplTest-context.xml")
public class RgbControllerImplTest
{
    @Autowired
    RgbController rgbController;

    @Test
    @Ignore
    public void runTest() throws RS232ControllerException, RgbControllerException
    {
        rgbController.setColor(0xFF, 0x00, 0x00);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
        }

        rgbController.setColor(0x00, 0xFF, 0xFF);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
        }

        rgbController.setColor(Color.BLACK);
    }
}
