/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.consumer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wengyz
 * @version
 */
@SpringBootApplication(scanBasePackages = {"com.consumer","com.yutian.client"})
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}