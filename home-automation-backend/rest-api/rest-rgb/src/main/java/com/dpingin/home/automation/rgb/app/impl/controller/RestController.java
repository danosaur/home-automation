package com.dpingin.home.automation.rgb.app.impl.controller;

import com.dpingin.home.automation.rgb.controller.api.color.Color;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbController;
import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
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
    protected RgbController rgbController;

    @RequestMapping(value = "/rgb", method = RequestMethod.POST)
    public @ResponseBody String setColor(@RequestParam(value = "r", required = true)int r,
                                     @RequestParam(value = "g", required = true)int g,
                                     @RequestParam(value = "b", required = true)int b) throws RgbControllerException
    {
        rgbController.setColor(r, g, b);
        return "Set color: " + r + ", " + g + ", " + b;
    }

    @RequestMapping(value = "/rgb", method = RequestMethod.GET)
    public @ResponseBody Color getColor()
    {
        return rgbController.getColor();
    }
}
