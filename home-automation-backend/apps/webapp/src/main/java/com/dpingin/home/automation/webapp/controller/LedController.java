package com.dpingin.home.automation.webapp.controller;

import com.dpingin.home.automation.pattern.api.Pattern;
import com.dpingin.home.automation.pattern.api.control.Control;
import com.dpingin.home.automation.pattern.api.control.Controls;
import com.dpingin.home.automation.pattern.api.switcher.PattenSwitcherException;
import com.dpingin.home.automation.pattern.api.switcher.PatternSwitcher;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 25.06.14
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/led")
public class LedController
{
	@Autowired
	private PatternSwitcher patternSwitcher;

	@Value("${pattern.default}")
	private String defaultPatternName;

	@PostConstruct
	public void init()
	{
		try
		{
			if (defaultPatternName != null)
				patternSwitcher.switchPattern(defaultPatternName);
		}
		catch (PattenSwitcherException e)
		{
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "/patterns/{name}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void switchPattern(@PathVariable(value = "name") String name) throws PattenSwitcherException
	{
		patternSwitcher.switchPattern(name);
	}

	@RequestMapping(value = "/patterns", method = RequestMethod.GET)
	@ResponseBody
	public List<Pattern> getPatterns()
	{
		return patternSwitcher.getPatterns();
	}

	@RequestMapping(value = "/patterns/current", method = RequestMethod.GET)
	@ResponseBody
	public Pattern getCurrentPattern()
	{
		return patternSwitcher.getCurrentPattern();
	}

	@RequestMapping(value = "/patterns/{name}/controls", method = RequestMethod.GET)
	public @ResponseBody
	Controls getControls(@PathVariable(value = "name") String name) throws PattenSwitcherException
	{
		Pattern pattern = patternSwitcher.getPattern(name);
		if (pattern == null)
			throw new PattenSwitcherException(String.format("Pattern not found: %s", name));
		return pattern.getControls();
	}

	@RequestMapping(value = "/patterns/{patternName}/controls", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void updateControls(@PathVariable(value = "patternName") String patternName,
	                           @RequestBody List<Control> controls) throws PattenSwitcherException
	{
		Pattern pattern = patternSwitcher.getPattern(patternName);
		pattern.updateControls(controls);
	}

	@RequestMapping(value = "/patterns/{patternName}/controls/{controlName}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String updateControls(@PathVariable(value = "patternName") String patternName,
	                             @PathVariable(value = "controlName") String controlName)
	{
		Pattern pattern = patternSwitcher.getPattern(patternName);
		return pattern.getControls().get(controlName).getValue().toString();
	}

	@RequestMapping(value = "/patterns/{patternName}/controls/{controlName}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void updateControls(@PathVariable(value = "patternName") String patternName,
	                           @PathVariable(value = "controlName") String controlName,
	                           @RequestBody String value) throws PattenSwitcherException
	{
		Pattern pattern = patternSwitcher.getPattern(patternName);
		pattern.getControls().get(controlName).setValueString(value);
	}

	@RequestMapping(value = "/rgb", method = RequestMethod.POST)
	public @ResponseBody
	Color setColor(@RequestBody Color color) throws RgbControllerException, PattenSwitcherException
	{
		patternSwitcher.getCurrentPattern().setColor(color);
		return color;
	}

	@RequestMapping(value = "/rgb", method = RequestMethod.GET)
	public @ResponseBody
	Color getColor()
	{
		return patternSwitcher.getCurrentPattern().getColor();
	}
}
