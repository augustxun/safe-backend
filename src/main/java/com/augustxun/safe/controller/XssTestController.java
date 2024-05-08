package com.augustxun.safe.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "XSS测试接口")
@RequestMapping("static")
@Slf4j
public class XssTestController {

    @RequestMapping("/demo")
    public String demo(HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        log.info("into demo page");
        return "demo";
    }

    @ResponseBody
    @RequestMapping("/demoAction")
    public String demoAction(@RequestParam(value = "name") String name,
                             HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        log.info("name:" + name);
        return "name is:" + name;
    }
}
