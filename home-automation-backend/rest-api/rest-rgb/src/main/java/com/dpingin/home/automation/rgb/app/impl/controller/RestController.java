package com.dpingin.home.automation.rgb.app.impl.controller;

import com.dpingin.home.automation.audio.api.pattern.switcher.PattenSwitcherException;
import com.dpingin.home.automation.audio.api.pattern.switcher.PatternSwitcher;
import com.dpingin.home.automation.audio.impl.pattern.StaticColorPattern;
import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    PatternSwitcher patternSwitcher;

    @RequestMapping(value = "/pattern")
    @ResponseStatus(HttpStatus.OK)
    public void switchPattern(@RequestParam(value = "p", required = true) String patternName) throws PattenSwitcherException
    {
        patternSwitcher.switchPattern(patternName);
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
