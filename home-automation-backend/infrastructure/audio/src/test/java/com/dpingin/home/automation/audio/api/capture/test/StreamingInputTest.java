//package com.dpingin.home.automation.audio.api.capture.test;
//
//import com.dpingin.home.automation.audio.api.pattern.PatternException;
//import com.dpingin.home.automation.audio.impl.pattern.AudioInputColorPattern;
//import com.dpingin.home.automation.audio.impl.pattern.StaticColorPattern;
//import com.dpingin.home.automation.rgb.controller.api.rgb.RgbControllerException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * Created with IntelliJ IDEA.
// * User: DanoSaur
// * Date: 14.06.14
// * Time: 11:40
// * To change this template use File | Settings | File Templates.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/StreamingInputTest-context.xml")
//public class StreamingInputTest
//{
//    private Logger log = LoggerFactory.getLogger(getClass());
//
//
//    protected long runTimeSeconds = 60 * 60 * 60;
//
//    @Autowired
//    protected AudioInputColorPattern audioInputColorPattern;
//
//    @Autowired
//    protected StaticColorPattern staticColorPattern;
//
//    @Test
//    public void sampleProcessorTest() throws RgbControllerException, InterruptedException, PatternException
//    {
//        audioInputColorPattern.init();
//        audioInputColorPattern.start();
//
//        long startTime = System.currentTimeMillis();
//        long elapsedTime = 0;
//        while (elapsedTime < 10000)
//        {
//            elapsedTime = System.currentTimeMillis() - startTime;
//            Thread.sleep(500);
//        }
//
//        staticColorPattern.start();
//
//        audioInputColorPattern.stop();
//        audioInputColorPattern.destroy();
//    }
//}
