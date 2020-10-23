/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.client.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
/**
 *
 * @author wengyz
 * @version RpcClientInitializer.java, v 0.1 2020-10-20 8:46 下午
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel socketChannel){
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        cp.addLast(new LengthFieldPrepender(4));
        cp.addLast(new StringDecoder(CharsetUtil.UTF_8));
        cp.addLast(new StringEncoder(CharsetUtil.UTF_8));
        cp.addLast(RpcClientHandler.getInstance());
    }
}