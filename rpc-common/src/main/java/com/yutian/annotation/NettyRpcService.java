/**
 * aljk.com
 * Copyright (C) 2013-2020All Rights Reserved.
 */
package com.yutian.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author wengyz
 * @version NettyRpcService.java, v 0.1 2020-10-20 5:20 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface NettyRpcService {
    Class<?> value();

    String version() default "";
}