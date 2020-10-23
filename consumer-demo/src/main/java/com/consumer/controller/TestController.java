/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.consumer.controller;

import com.yutian.annotation.RpcAutowired;
import com.yutian.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author wengyz
 * @version TestController.java, v 0.1 2020-10-21 7:46 下午
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RpcAutowired(version = "1.0")
    private TestService testService;


    @RequestMapping("/demo")
    public String test(){
        String result = testService.say("consumer");
        System.out.println("result = " + result);
        return result;
    }
}