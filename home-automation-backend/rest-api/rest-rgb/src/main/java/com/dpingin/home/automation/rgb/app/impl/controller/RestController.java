package com.dpingin.home.automation.rgb.app.impl.controller;

import com.dpingin.home.automation.audio.api.pattern.Pattern;
import com.dpingin.home.automation.audio.api.pattern.control.AbstractControl;
import com.dpingin.home.automation.audio.api.pattern.control.Control;
import com.dpingin.home.automation.audio.api.pattern.control.Controls;
import com.dpingin.home.automation.audio.api.pattern.switcher.PattenSwitcherException;
import com.dpingin.home.automation.audio.api.pattern.switcher.PatternSwitcher;
import com.dpingin.home.automation.audio.impl.pattern.StaticColorPattern;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 25.06.14
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/rgb")
public class RestController
{
    @Resource
    protected StaticColorPattern staticColorPattern;
    @Resource
    protected PatternSwitcher patternSwitcher;
    @Resource
    protected String defaultPatternName;

    @PostConstruct
    public void init()
    {
        try
        {
            if (defaultPatternName != null)
                patternSwitcher.switchPattern(defaultPatternName);
        } catch (PattenSwitcherException e)
        {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/pattern")
    @ResponseStatus(HttpStatus.OK)
    public void switchPattern(@RequestParam(value = "p", required = true) String patternName) throws PattenSwitcherException
    {
        patternSwitcher.switchPattern(patternName);
    }

    @RequestMapping(value = "/pattern/{name}/controls", method = RequestMethod.GET)
    public @ResponseBody Controls getControls(@PathVariable(value = "name") String name) throws PattenSwitcherException
    {
        Pattern pattern = patternSwitcher.getPattern(name);
        if (pattern == null)
            throw new PattenSwitcherException(String.format("Pattern not found: %s", name));
        return pattern.getControls();
    }

    @RequestMapping(value = "/pattern/{name}/controls", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateControls(@PathVariable(value = "name") String name, @RequestBody List<Control> controls) throws PattenSwitcherException
    {
        Pattern pattern = patternSwitcher.getPattern(name);
        pattern.updateControls(controls);
    }

    @RequestMapping(value = "/rgb", method = RequestMethod.POST)
    public
    @ResponseBody
    String setColor(@RequestParam(value = "r", required = true) int r,
                    @RequestParam(value = "g", required = true) int g,
                    @RequestParam(value = "b", required = true) int b) throws RgbControllerException, PattenSwitcherException
    {
        staticColorPattern.setColor(new Color(r, g, b));
        return "Set color: " + r + ", " + g + ", " + b;
    }

    @RequestMapping(value = "/rgb", method = RequestMethod.GET)
    public
    @ResponseBody
    Color getColor()
    {
        return staticColorPattern.getColor();
    }
}
