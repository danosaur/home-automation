package com.dpingin.home.automation.rgb.controller.configuration;

import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rs232.RS232Controller;
import com.dpingin.home.automation.rgb.controller.impl.rgb.RgbControllerImpl;
import com.dpingin.home.automation.rgb.controller.impl.rs232.RS232ControllerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RgbControllerConfiguration
{
	@Value("${rgb.controller.portName}")
	private String portName;

	@Bean
	public RgbController rgbController()
	{
		RgbControllerImpl rgbController = new RgbControllerImpl();
		rgbController.setRs232Controller(rs232Controller());
		rgbController.setPortName(portName);
		return rgbController;
	}

	@Bean
	public RS232Controller rs232Controller()
	{
		return new RS232ControllerImpl();
	}
}
