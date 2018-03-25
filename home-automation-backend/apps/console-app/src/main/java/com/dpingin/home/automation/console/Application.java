/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dpingin.home.automation.console;

import com.dpingin.home.automation.console.input.InputHandler;
import com.dpingin.home.automation.rgb.controller.configuration.RgbControllerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
@Import(RgbControllerConfiguration.class)
public class Application
{
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args)
	{
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Application.class, args);

		InputHandler inputHandler = configurableApplicationContext.getBean(InputHandler.class);
		try
		{
			inputHandler.handleInput(args);
		}
		catch (Exception e)
		{
			log.error("Unhandled error occurred", e);
		}

		while (true)
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			try
			{
				String input = bufferedReader.readLine();

				inputHandler.handleInput(input.trim().split(" "));
			}
			catch (Exception e)
			{
				log.error("Unhandled error occurred", e);
				break;
			}
		}
	}
}
