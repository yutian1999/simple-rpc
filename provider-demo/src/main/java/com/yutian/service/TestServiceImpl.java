/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.service;

import com.yutian.annotation.NettyRpcService;

/**
 *
 * @author wengyz
 * @version TestServiceImpl.java, v 0.1 2020-10-21 8:19 下午
 */
@NettyRpcService(value = TestService.class,version = "1.0")
public class TestServiceImpl implements TestService {
    @Override
    public String say(String name) {
        System.out.println("hello " + name);
        return "hello " + name;
    }
}