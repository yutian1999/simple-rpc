/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.server;

import com.yutian.annotation.NettyRpcService;
import com.yutian.server.core.NettyServer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @author wengyz
 * @version RpcServer.java, v 0.1 2020-10-20 4:23 下午
 */
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    private NettyServer nettyServer;

    @Value("${rpc.port}")
    private Integer port;

    @Override
    public void destroy(){
        nettyServer.stop();
    }

    @Override
    public void afterPropertiesSet(){
        nettyServer.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(NettyRpcService.class);
        nettyServer = new NettyServer(port);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                NettyRpcService nettyRpcService = serviceBean.getClass().getAnnotation(NettyRpcService.class);
                String interfaceName = nettyRpcService.value().getName();
                String version = nettyRpcService.version();
                nettyServer.addService(interfaceName, version, serviceBean);
            }
        }
    }
}