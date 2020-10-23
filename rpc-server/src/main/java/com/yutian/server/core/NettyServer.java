/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.server.core;

import com.yutian.server.util.ServiceUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wengyz
 * @version NettyServer.java, v 0.1 2020-10-20 4:24 下午
 */
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private Integer port;

    public NettyServer(Integer port) {
        this.port = port;
    }

    private Map<String, Object> serviceMap = new HashMap();

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void addService(String interfaceName, String version, Object serviceBean) {
        logger.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
        String serviceKey = ServiceUtil.makeServiceKey(interfaceName, version);
        serviceMap.put(serviceKey, serviceBean);
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                            .childHandler(new RpcServerInitializer(serviceMap));
                    ChannelFuture future = bootstrap.bind(port).sync();
                    future.channel().closeFuture().sync();
                    logger.info("Server started on port {}", port);
                } catch (Exception e) {
                    logger.error("Rpc server remoting server error", e);
                } finally {
                    stop();
                }
            }
        }).start();
    }

    public void stop() {
        try {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}