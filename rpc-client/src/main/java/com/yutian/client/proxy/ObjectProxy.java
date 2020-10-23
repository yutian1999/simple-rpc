package com.yutian.client.proxy;
import com.yutian.client.core.RpcClientHandler;
import com.yutian.transfer.RpcRequest;
import com.yutian.transfer.RpcResponse;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 *
 * @author wengyz
 * @version RpcClient.java, v 0.1 2020-10-20 9:33 下午
 */
public class ObjectProxy<T, P> implements InvocationHandler {
    private Class<T> clazz;
    private String version;

    public ObjectProxy(Class<T> clazz, String version) {
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        request.setVersion(version);
        RpcResponse response = RpcClientHandler.sendRequest(request);
        if (!StringUtils.isEmpty(response.getError())){
            throw new RuntimeException("rpc invoke error");
        }
        return response.getResult();
    }
}
