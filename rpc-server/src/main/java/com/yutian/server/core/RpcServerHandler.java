/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.server.core;
import com.alibaba.fastjson.JSON;
import com.yutian.server.util.ServiceUtil;
import com.yutian.transfer.RpcRequest;
import com.yutian.transfer.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 * @author wengyz
 * @version RpcServerHandler.java, v 0.1 2020-10-20 4:52 下午
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final String request) throws InvocationTargetException {
        RpcResponse handle = handle(JSON.parseObject(request, RpcRequest.class));
        ctx.channel().writeAndFlush(JSON.toJSONString(handle));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("Server caught exception: " + cause.getMessage());
        ctx.close();
    }

    private RpcResponse handle(RpcRequest request) throws InvocationTargetException {
        String className = request.getClassName();
        String version = request.getVersion();
        String serviceKey = ServiceUtil.makeServiceKey(className, version);
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            logger.error("Can not find service implement with interface name: {} and version: {}", className, version);
            return null;
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        FastClass serviceFastClass = FastClass.create(serviceClass);
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        Object invoke = serviceFastClass.invoke(methodIndex, serviceBean, parameters);
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        response.setResult(invoke);
        return response;
    }
}