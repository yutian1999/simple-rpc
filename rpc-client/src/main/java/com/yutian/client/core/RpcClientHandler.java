/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.client.core;
import com.alibaba.fastjson.JSON;
import com.yutian.transfer.RpcRequest;
import com.yutian.transfer.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wengyz
 * @version RpcClientHandler.java, v 0.1 2020-10-20 8:57 下午
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<String> {

    private static Channel channel;

    private static Map<String,RpcResponse> result = new HashMap<>();

    private RpcClientHandler(){
    }

    private static RpcClientHandler instance = new RpcClientHandler();

    public static RpcClientHandler getInstance(){
        return instance;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String rpcResponse){
        RpcResponse response = JSON.parseObject(rpcResponse, RpcResponse.class);
        result.put(response.getRequestId(),response);
    }

    public static RpcResponse sendRequest(final RpcRequest request){
        channel.writeAndFlush(JSON.toJSONString(request));
        long start = System.currentTimeMillis();
        while (true){
            if (result.containsKey(request.getRequestId())){
                break;
            }

            if ((System.currentTimeMillis() - start) / 1000 > 15){
                break;
            }
        }

        if (result.containsKey(request.getRequestId())){
            RpcResponse response = result.get(request.getRequestId());
            result.remove(request.getRequestId());
            return response;
        }

        RpcResponse response = new RpcResponse();
        response.setError("time out");
        response.setResult("time out");
        return response;
    }
}