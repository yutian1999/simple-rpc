/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.client.core;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wengyz
 * @version RpcClient.java, v 0.1 2020-10-20 8:28 下午
 */
public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String address;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(String address) {
        this.address = address;
    }

    public void start() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                            .handler(new RpcClientInitializer());
                    String[] rAddress = address.split(":");
                    ChannelFuture future = bootstrap.connect(rAddress[0], Integer.parseInt(rAddress[1])).sync();
                    future.channel().closeFuture().sync();
                    logger.info("client connect server success, port {}", Integer.parseInt(rAddress[1]));
                } catch (Exception e) {
                    logger.error("no service provider error ={}",e.getMessage());
                    throw new RuntimeException("no service provider");
                } finally {
                    try {
                        workerGroup.shutdownGracefully();
                    } catch (Exception e) {
                        logger.error("no service provider error ={}",e.getMessage());
                        throw new RuntimeException("no service provider");
                    }
                }
            }
        }).start();
    }
    public void stop() {
        try {
            workerGroup.shutdownGracefully();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}